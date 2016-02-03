/*
 * Copyright 2016 Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.eti.jadler.nsis;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguaraquem.adler@biologica.com.br>
 */
public class Field implements Comparable<Field> {

    private final Integer field;
    private final String type;
    private final String text;
    private final Integer left;
    private final Integer right;
    private final Integer top;
    private final Integer bottom;
    private final Integer state;

    public Field(Integer id, String type, String text, Integer left, Integer right, Integer top, Integer bottom, Integer state) {
        this.field = id;
        this.type = type;
        this.text = text;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.state = state;
    }

    @Override
    public String toString() {
        String str = "[Field " + field + ']';

        str += (type != null) ? "\nType=" + type : "";
        str += (text != null) ? "\nText=" + text : "";
        str += (left != null) ? "\nLeft=" + left : "";
        str += (right != null) ? "\nRight=" + right : "";
        str += (top != null) ? "\nTop=" + top : "";
        str += (bottom != null) ? "\nBottom=" + bottom : "";
        str += (state != null) ? "\nState=" + state : "";

        return str;
    }

    @Override
    public int compareTo(Field o) {
        return this.field - o.field;
    }
}
