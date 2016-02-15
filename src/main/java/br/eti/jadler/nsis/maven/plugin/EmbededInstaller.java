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
package br.eti.jadler.nsis.maven.plugin;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
public class EmbededInstaller {

    private final String installer;
    private final String directory;
    private final String[] extraParams;
    private final Boolean addToPath;
    private final Boolean forAllUsers;

    public EmbededInstaller(String installer, String directory, Boolean addToPath, Boolean forAllUsers, String... extraParams) {
        this.installer = installer;
        this.directory = directory;
        this.addToPath = addToPath;
        this.forAllUsers = forAllUsers;
        this.extraParams = extraParams;
    }

    @Override
    public String toString() {
        String execWait = "";
        execWait += "File /oname=\\$PLUGINSDIR\\\\" + installer + " \"" + installer + "\"\n";
        execWait += "ExecWait \"\\$PLUGINSDIR\\\\" + installer;

        for (String extraParam : extraParams) {
            execWait += " " + extraParam;
        }

        if (directory != null) {
            execWait += " /D=" + directory;
        }

        execWait += "\"";
        
        if (addToPath && forAllUsers) {
            
        } else if (addToPath && !forAllUsers) {
            
        }

        return execWait;
    }
}
