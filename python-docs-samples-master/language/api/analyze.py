#!/usr/bin/env python

# Copyright 2016 Google, Inc
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Analyzes text using the Google Cloud Natural Language API."""

import argparse
import json
import sys
import time

import googleapiclient.discovery

import mysql.connector
from mysql.connector import errorcode

def get_native_encoding_type():
    """Returns the encoding type that matches Python's native strings."""
    if sys.maxunicode == 65535:
        return 'UTF16'
    else:
        return 'UTF32'


def analyze_entities(text, encoding='UTF32'):
    body = {
        'document': {
            'type': 'PLAIN_TEXT',
            'content': text,
        },
        'encoding_type': encoding,
    }

    service = googleapiclient.discovery.build('language', 'v1')

    request = service.documents().analyzeEntities(body=body)
    response = request.execute()

    return response


def analyze_sentiment(text, encoding='UTF32'):
    body = {
        'document': {
            'type': 'PLAIN_TEXT',
            'content': text,
        },
        'encoding_type': encoding
    }

    service = googleapiclient.discovery.build('language', 'v1')

    request = service.documents().analyzeSentiment(body=body)
    response = request.execute()

    return response


def analyze_syntax(text, encoding='UTF32'):
    body = {
        'document': {
            'type': 'PLAIN_TEXT',
            'content': text,
        },
        'encoding_type': encoding
    }

    service = googleapiclient.discovery.build('language', 'v1')

    request = service.documents().analyzeSyntax(body=body)
    response = request.execute()

    return response


if __name__ == '__main__':
    while True:
        try:
            cnx = mysql.connector.connect(user='bruno',
                                          password='bruno',
                                          database='testekkk')
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Something is wrong with your user name or password")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print(err)
        else:
            cursor = cnx.cursor()
            cursor.execute("select count(*) from comment where pontos IS NULL;");
            nlinha = cursor.fetchone();
            print('comentarios sem nota: ', nlinha[0])
            cursor.execute("select comment from comment where pontos IS NULL;");
            linha = cursor.fetchall();

        for loop in range(nlinha[0]):

            parser = argparse.ArgumentParser(
                description=__doc__,
                formatter_class=argparse.RawDescriptionHelpFormatter)
            parser.add_argument('command', choices=[
                'entities', 'sentiment', 'syntax'])

            parser.add_argument('text')

            args = parser.parse_args()
            com = ''.join(linha[loop]);
            args.text = com;

            if args.command == 'entities':
                result = analyze_entities(args.text, get_native_encoding_type())
            elif args.command == 'sentiment':
                result = analyze_sentiment(args.text, get_native_encoding_type())
            elif args.command == 'syntax':
                result = analyze_syntax(args.text, get_native_encoding_type())

            result = json.dumps(result)
            print("comentario = ", com)

            start = result.find("tude") + 7
            end = result.find("score") - 3
            magnitude = result[start:end]
            start = result.find("score") + 8
            end = result.find("language") -4
            score = result[start:end]
            print("magnitude = ", magnitude);
            print("score = ", score);
            ponto = float(magnitude)*float(score);
            pontos = ("%.2f" % ponto)
            print("pontuação final = ", pontos);
            print("");

            cursor.execute ("""
            UPDATE comment
            SET pontos=%s
            WHERE comment=%s
            """, (str(pontos), com))
            cnx.commit();

        cnx.close()
        time.sleep(10)
