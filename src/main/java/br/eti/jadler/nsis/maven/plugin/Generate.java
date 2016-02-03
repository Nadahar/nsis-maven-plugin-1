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

import br.eti.jadler.nsis.Field;
import br.eti.jadler.nsis.InstallOption;
import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class Generate extends AbstractMojo {

//    @Parameter(defaultValue = "${project}", required = true, readonly = true)
//    private MavenProject project;
//
//    @Parameter(defaultValue = "${project.basedir}", readonly = true)
//    private File basedir;
//
//    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
//    private File target;
//
    @Parameter(defaultValue = "${project.build.directory}/InstallOption.ini")
    private File installOptionFile;

    @Parameter(defaultValue = "false")
    private Boolean overwriteInstallOptionFile;
//
//    @Parameter(defaultValue = "$PROGRAMFILES", required = true)
//    private String installRoot;
//    
//    @Parameter(property = "nsis.muiIcon")
//    private String icon;
//    
//    @Parameter(property = "nsis.muiUnIcon")
//    private String unIcon;
//    
//    @Parameter
//    private String extraPreInstallCommand;
//    
//    @Parameter
//    private String extraInstallCommand;
//    
//    @Parameter
//    private String extraUninstallCommand;
//    
//    @Parameter
//    private Boolean enableUninstallBeforeInstall;
//    
//    @Parameter
//    private Boolean modifyPath;
//    
//    @Parameter
//    private String displayName;
//    
    @Parameter
    private String packageName;
//    
//    @Parameter(defaultValue = "${project.build.finalName}")
//    private String packageFileName;
//    
//    @Parameter
//    private URL helpLink;
//    
//    @Parameter
//    private URL urlInfoAbout;
//    
//    @Parameter
//    private String contact;
//    
    @Parameter
    private String installDirectory;
//    
//    @Parameter
//    private String vendor;
//    
//    @Parameter
//    private File outputFileName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (overwriteInstallOptionFile) {
            genInstallOption();
        }
    }

    private void genInstallOption() throws MojoExecutionException {
        final InstallOption options = new InstallOption();

        final String label = "By default " + installDirectory + " does not add its directory to the system PATH.";
        final String doNotAddInPath = "Do not add " + packageName + " to the system PATH";
        final String addForAllUsers = "Add " + packageName + " to the system PATH for all users";
        final String addForCurrentUser = "Add " + packageName + " to the system PATH for current user";
        final String checkbox = "Create " + packageName + " Desktop Icon";

        Field f1 = new Field(1, "label", label, 0, -1, 0, 20, null);
        Field f2 = new Field(2, "radionbutton", doNotAddInPath, 0, -1, 30, 40, 1);
        Field f3 = new Field(3, "radionbutton", addForAllUsers, 0, -1, 40, 50, 0);
        Field f4 = new Field(4, "radionbutton", addForCurrentUser, 0, -1, 50, 60, 0);
        Field f5 = new Field(5, "CheckBox", checkbox, 0, -1, 80, 90, 0);

        try {
            options.writeToFile(installOptionFile);
        } catch (IOException ex) {
            throw new MojoExecutionException("Unable to generate project script " + installOptionFile.getAbsolutePath(), ex);
        }
    }

}
