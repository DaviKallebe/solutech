package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import co.chatsdk.core.dao.Message;
import co.chatsdk.core.events.EventType;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.types.ReadStatus;
import co.chatsdk.core.utils.CrashReportingCompletableObserver;
import co.chatsdk.core.utils.DisposableList;
import co.chatsdk.core.utils.PermissionRequestHandler;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.ui.helpers.NotificationUtils;
import co.chatsdk.ui.login.LoginActivity;
import co.chatsdk.ui.manager.InterfaceManager;
import co.chatsdk.ui.manager.UserInterfaceModule;
import co.chatsdk.ui.threads.PrivateThreadsFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity implements
        EditarValorFragment.OnFragmentInteractionListener,
        HospedadorListagemFragment.OnFragmentInteractionListener,
        HospedadorServicoFragment.OnFragmentInteractionListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PET_PICK_IMAGE = 2;
    private static final String read_file_perm = "android.permission.READ_EXTERNAL_STORAGE";

    private MainActivityViewModel mViewModel;
    private Integer id_user;
    private FirebaseAuth mAuth;
    private DisposableList disposables = new DisposableList();
    private List<FragmentOption> fragmentOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.logado_toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("userfile", MODE_PRIVATE);
        id_user = prefs.getInt("id_user", 0);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.loadCurrentUser(id_user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();

        fragmentManager.addOnBackStackChangedListener(() -> {
            if (actionBar != null) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                }
                else {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowHomeEnabled(false);
                }
            }
        });

        fragmentOptions = new ArrayList<>();

        fragmentOptions.add(new FragmentOption(HospedadorListagemFragment.class,
                getResources().getString(R.string.fragment_hospedador_listagem), null));
        fragmentOptions.add(new FragmentOption(PrivateThreadsFragment.class,
                getResources().getString(R.string.fragment_hospedador_mensagem), null));
        fragmentOptions.add(new FragmentOption(HospedadorServicoFragment.class,
                getResources().getString(R.string.fragment_hospedador_servico), null));

        //
        Context context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        Configuration.Builder builder = new Configuration.Builder(context);

        builder.firebaseRootPath("nossochat");
        ChatSDK.initialize(builder.build());
        UserInterfaceModule.activate(context);

        FirebaseModule.activate();
        FirebaseFileStorageModule.activate();

        mViewModel.getCurrentUser().observe(this, resource -> {
            if (resource != null && resource.status == Status.SUCCESS) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Usuario user = resource.data;
                LoginActivity loginActivity = new LoginActivity();

                loginActivity.authenticateWithAppUser(currentUser, this, user.getFullName(), user.getImagem());
                ((PrivateThreadsFragment)fragmentOptions.get(1).getFragment()).setTabVisibility(true);

                requestPermissionSafely(requestExternalStorage().doFinally(() -> requestPermissionSafely(requestMicrophoneAccess().doFinally(() -> requestPermissionSafely(requestReadContacts().doFinally(() -> {
                    //requestVideoAccess().subscribe();
                }))))));
            }
        });

        ViewPager mPager = findViewById(R.id.main_activity_viewPager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, fragmentOptions);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.main_activity_tablayout);
        tabLayout.setupWithViewPager(mPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                ((PrivateThreadsFragment)fragmentOptions.get(1).getFragment()).setTabVisibility(1 == tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_perfil) {
            startPerfilFragment();

            return true;
        } else if (id == R.id.action_deslogar) {
            Disposable disposable = NM.auth().logout()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();

                        Intent it = new Intent(MainActivity.this, Login.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(it);
                    }, throwable -> {
                        Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

            return true;
        }
        else if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStackImmediate();

            return true;
        }
        else if (id == R.id.action_pedido) {
            PedidoListagemFragment pedidoListagemFragment = new PedidoListagemFragment();

            startFragment(pedidoListagemFragment, null);
        }
        else if (id == R.id.action_mensagem) {
            InterfaceManager.shared().a.startMainActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPerfilFragment() {
        Fragment usuarioPerfil = new UsuarioPerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        usuarioPerfil,
                        usuarioPerfil.getClass().getSimpleName())
                .addToBackStack(getResources().getString(R.string.perfil_fragment))
                .commit();
    }

    @Override
    public void startFragment(Fragment fragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        fragment,
                        fragment.getClass().getSimpleName())
                .addToBackStack(name)
                .commit();
    }

    @Override
    public void startServicoFragment(Fragment fragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        fragment,
                        fragment.getClass().getSimpleName())
                .addToBackStack(name)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

            if (mViewModel != null)
                mViewModel.updateProfile(id_user, stream);
        }
        else
        if (requestCode == PET_PICK_IMAGE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = getPicture(data.getData());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

            Integer imageSize = stream.toByteArray().length / 1024;

            if (imageSize > 1024)
                Toast.makeText(this, "Imagem muito grande", Toast.LENGTH_SHORT).show();

            Pet pet = new Pet();

            if (mViewModel != null && imageSize <= 1024)
                mViewModel.updateUserPet(pet, getFileName(data.getData()), stream);
        }
    }

    public String getFileName(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            Integer columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            return picturePath.substring(picturePath.lastIndexOf("/")+1);
        }

        return null;
    }

    public Bitmap getPicture(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            Integer columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            BitmapFactory.Options opt = new BitmapFactory.Options();

            cursor.close();

            return BitmapFactory.decodeFile(picturePath, opt);
        }

        return null;
    }

    @Override
    public <T> void saveFieldOnBackend(String fieldName, T fieldValue) {
        if (mViewModel != null)
            mViewModel.updateProfile(id_user, fieldName, fieldValue);
    }

    private class FragmentOption {
        private Class classFragment;
        private String fragmentName;
        private Bundle bundle;

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
        }

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName, Bundle bundle) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
            this.bundle = bundle;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public Fragment getFragment() {
            try {
                Fragment fragment = (Fragment) classFragment.newInstance();

                if (fragment instanceof  PrivateThreadsFragment)
                    ((PrivateThreadsFragment) fragment).setTabVisibility(true);

                fragment.setArguments(bundle);

                return fragment;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposables.add(NM.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.MessageAdded))
                .filter(NetworkEvent.filterThreadType(ThreadType.Private))
                .subscribe(networkEvent -> {
                    Message message = networkEvent.message;
                    if(message != null) {
                        if(!message.getSender().isMe() && InterfaceManager.shared().a.showLocalNotifications()) {
                            ReadStatus status = message.readStatusForUser(NM.currentUser());
                            if (!message.isRead() && !status.is(ReadStatus.delivered())) {
                                // Only show the alert if we'recyclerView not on the private threads tab
                                NotificationUtils.createMessageNotification(MainActivity.this, networkEvent.message);
                            }
                        }
                    }
                }));

        disposables.add(NM.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.Logout))
                .subscribe(networkEvent -> clearData()));


        reloadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.dispose();
    }

    public void clearData () {
        ((PrivateThreadsFragment)fragmentOptions.get(1).getFragment()).clearData();
    }

    public void reloadData () {
        ((PrivateThreadsFragment)fragmentOptions.get(1).getFragment()).safeReloadData();
    }

    public void requestPermissionSafely (Completable c) {
        c.subscribe(new CrashReportingCompletableObserver());
    }

    public Completable requestMicrophoneAccess () {
        if(NM.audioMessage() != null) {
            return PermissionRequestHandler.shared().requestRecordAudio(this);
        }
        return Completable.complete();
    }

    public Completable requestExternalStorage () {
//        if(NM.audioMessage() != null) {
        return PermissionRequestHandler.shared().requestReadExternalStorage(this);
//        }
//        return Completable.complete();
    }

    public Completable requestVideoAccess () {
        if(NM.videoMessage() != null) {
            return PermissionRequestHandler.shared().requestVideoAccess(this);
        }
        return Completable.complete();
    }

    public Completable requestReadContacts () {
        if(NM.contact() != null) {
            return PermissionRequestHandler.shared().requestReadContact(this);
        }
        return Completable.complete();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionRequestHandler.shared().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<FragmentOption> fragmentOptions;

        ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<FragmentOption> fragmentOptions) {
            super(fragmentManager);
            this.fragmentOptions = fragmentOptions;
        }

        @Override
        public Fragment getItem(int position) {


            return fragmentOptions.get(position).getFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentOptions.get(position).getFragmentName();
        }

        @Override
        public int getCount() {
            return fragmentOptions.size();
        }
    }
}