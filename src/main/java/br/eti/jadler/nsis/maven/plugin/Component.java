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

import java.util.ArrayList;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
public class Component {

    private String name;
    private String outputPath;
    private final ArrayList<String> includes = new ArrayList<>();
//    private final ArrayList<String> excludes = new ArrayList<>();

    @Override
    public String toString() {

        outputPath = outputPath.replace("/", "\\\\");

        String component = "";
        component += "Section \"-" + name + "\" " + name + "\n";
        component += "\tSectionIn RO\n";
        component += "\tSetOutPath \"" + outputPath + "\"\n";
        component += "\tFile /r ";
        for (String include : includes) {
            component += "\"" + include + "\" ";
        }
        component += "\nSectionEnd\n";
        component += "!macro Remove_\\${" + name + "}\n";
        component += "\tIntCmp \\$" + name + "_was_installed 0 noremove_" + name + "\n";
        for (String include : includes) {
            component += "\tDelete \"" + outputPath + "\\" + include + "\"\n";
        }
        /*
         * Apaga recursivamente, se apontar para $PROGRAMFILE ou $PROGRAMDATA o
         * estrago será grande
         */
        //component += "\tRMDir /r " + outputPath + "\n";
        component += "\tnoremove_" + name + ":\n";
        component += "!macroend\n";
        component += "!macro Select_" + name + "_depends\n";
        component += "!macroend\n";
        component += "!macro Deselect_required_by_" + name + "\n";
        component += "!macroend\n\n";

        return component;
    }

//    private File[] filter(File root, String regex) {
//        if (!root.isDirectory()) {
//            throw new IllegalArgumentException(root + " is not a directory");
//        }
//
//        final Pattern p = Pattern.compile(regex);
//        return root.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return p.matcher(file.getName()).matches();
//            }
//        });
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
