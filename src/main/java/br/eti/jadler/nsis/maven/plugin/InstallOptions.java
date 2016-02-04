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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
public class InstallOption extends ArrayList<Field> {

    public final void writeToFile(final File file) throws IOException {
        try (final BufferedWriter bw = new BufferedWriter(
                new FileWriter(file.getAbsoluteFile()))) {

            Collections.sort(this);

            bw.write("[Settings]");
            bw.write("\nNumFields=" + size());
            for (Field field : this) {
                bw.write("\n\n");
                bw.write(field.toString());
            }
        }

        System.out.println("Done");

    }

    public final void writeToFile(final String file) throws IOException {
        writeToFile(new File(file));
    }
}
