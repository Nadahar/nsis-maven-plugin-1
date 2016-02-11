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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = false)
public class GenerateMojo extends AbstractMojo {

    final StringList fullInstall = new StringList();
    final StringList deleteFiles = new StringList();
    final StringList removeDirs = new StringList();

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(property = "nsis.installOption", defaultValue = "${project.build.directory}/InstallOptions.ini")
    private File installOptions;

    /**
     * Overwrite existing files.
     */
    @Parameter(property = "nsis.overwriteInstallOptionFile", defaultValue = "false")
    private Boolean overwrite;

    /**
     * Installation directory on the target system.
     */
    @Parameter(property = "nsis.installDirectory", required = true)
    private String installDirectory;

    /**
     * The arguments that will be passed to the NSIS SetCompressor command.
     */
    @Parameter(property = "nsis.compressor")
    private Compressor compressor;

    /**
     * Contact information for questions and comments about the installation
     * process.
     */
    @Parameter(property = "nsis.contact")
    private String contact;

    /**
     * The title displayed at the top of the installer and in the Windows
     * Add/Remove Program control panel.
     */
    @Parameter(property = "nsis.displayName", defaultValue = "${project.artifactId}")
    private String displayName;

    /**
     * The name of the package file to generate, not including the extension.
     * The default value is: ${project.build.finalName}
     */
    @Parameter(property = "nsis.packageName", defaultValue = "${project.build.finalName}")
    private String packageName;

    /**
     * The default installation directory presented to the end user by the NSIS
     * installer is under this root dir. The full directory presented to the end
     * user is: ${nsis.installRoot}/${nsis.installDirectory}
     */
    @Parameter(property = "nsis.installRoot", defaultValue = "\\$PROGRAMFILES", required = true)
    private String installRoot;

    /**
     * An icon filename. The name of a *.ico file used as the main icon for the
     * generated install program.
     */
    @Parameter(property = "nsis.muiIcon", defaultValue = "\\${NSISDIR}/Contrib/Graphics/Icons/modern-install.ico")
    private String muiIcon;

    /**
     * An icon filename. The name of a *.ico file used as the main icon for the
     * generated uninstall program.
     */
    @Parameter(property = "nsis.muiUnIcon", defaultValue = "\\${NSISDIR}/Contrib/Graphics/Icons/modern-uninstall.ico")
    private String muiUnIcon;

    /**
     * Bitmap image to display on the header of installers pages (recommended
     * size: 150x57 pixels).
     */
    @Parameter(property = "nsis.muiHeader", defaultValue = "\\${NSISDIR}/Contrib/Graphics/Header/nsis.bmp")
    private String muiHeader;

    /**
     * Bitmap for the Welcome page and the Finish page (recommended size:
     * 164x314 pixels).
     */
    @Parameter(property = "nsis.muiPanel", defaultValue = "\\${NSISDIR}/Contrib/Graphics/Wizard/win.bmp")
    private String muiPanel;

    /**
     * Bitmap image to be used as a splash screen.
     */
    @Parameter(property = "nsis.splash")
    private String splash;

    /**
     * Ask about uninstalling previous versions first. If this is set to “ON”,
     * then an installer will look for previous installed versions and if one is
     * found, ask the user whether to uninstall it before proceeding with the
     * install.
     */
    @Parameter(property = "nsis.enableUninstallBeforeInstall", defaultValue = "true")
    private Boolean enableUninstallBeforeInstall;

    /**
     * Modify PATH toggle. If this is set to “ON”, then an extra page will
     * appear in the installer that will allow the user to choose whether the
     * program directory should be added to the system PATH variable.
     */
    @Parameter(property = "nsis.modifyPath", defaultValue = "true")
    private Boolean modifyPath;

    /**
     * The name of the package file to generate, including the extension. The
     * default value is: ${project.build.finalName}.exe
     */
    @Parameter(property = "nsis.outputFileName", defaultValue = "${project.build.directory}/${project.build.finalName}.exe")
    private String outputFileName;

    /**
     * URL to a web site providing assistance in installing your application.
     */
    @Parameter(property = "nsis.helpLink")
    private String helpLink;

    /**
     * URL to a web site providing more information about your application.
     */
    @Parameter(property = "nsis.urlInfoAbout")
    private String urlInfoAbout;

    /**
     * The name of the package vendor.
     */
    @Parameter(property = "nsis.packageVendor")
    private String packageVendor;

    @Parameter(property = "nsis.packageVersion", defaultValue = "${project.version}", required = true)
    private String packageVersion;

    @Parameter(property = "nsis.licenseFile", defaultValue = "${project.basedir}/LICENSE")
    private String licenseFile;

//    @Parameter(property = "nsis.components")
//    private ArrayList<Component> components;
    @Parameter
    private String[] includes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!installOptions.exists()) {
            getLog().info("Generating " + installOptions.getAbsolutePath());
            genInstallOption();
        } else if (overwrite) {
            getLog().info("Overwriting " + installOptions.getAbsolutePath());
            genInstallOption();
        }

        genProjectScript();

    }

    private void genInstallOption() throws MojoExecutionException {
        final InstallOptions options = new InstallOptions();

        final String label = "By default " + installDirectory + " does not add its directory to the system PATH.";
        final String doNotAddInPath = "Do not add " + displayName + " to the system PATH";
        final String addForAllUsers = "Add " + displayName + " to the system PATH for all users";
        final String addForCurrentUser = "Add " + displayName + " to the system PATH for current user";
        final String checkbox = "Create " + displayName + " Desktop Icon";

        options.add(new Field(1, "label", label, 0, -1, 0, 20, null));
        options.add(new Field(2, "radionbutton", doNotAddInPath, 0, -1, 30, 40, 1));
        options.add(new Field(3, "radionbutton", addForAllUsers, 0, -1, 40, 50, 0));
        options.add(new Field(4, "radionbutton", addForCurrentUser, 0, -1, 50, 60, 0));
        options.add(new Field(5, "CheckBox", checkbox, 0, -1, 80, 90, 0));

        try {
            options.writeToFile(installOptions);
        } catch (IOException ex) {
            throw new MojoExecutionException("Unable to generate project script " + installOptions.getAbsolutePath(), ex);
        }
    }

    private void genProjectScript() throws MojoExecutionException {

        final URL template = this.getClass().getClassLoader().getResource("template.nsh");
        final String buildDirectory = project.getBuild().getDirectory();
        final File file = new File(buildDirectory + "/project.nsi");
        getLog().info("Generating " + file.getAbsolutePath() + " from " + template.getPath());

        try {

            FileUtils.copyURLToFile(template, file);
            final Charset charset = StandardCharsets.UTF_8;
            String templateContent = new String(Files.readAllBytes(file.toPath()), charset);

            final String licenseMacro;
            if (new File(licenseFile).exists()) {
                licenseMacro = "!insertmacro MUI_PAGE_LICENSE " + licenseFile;
            } else {
                licenseMacro = "";
            }

            if (includes != null) {

                getLog().debug("Files to be included: " + Arrays.toString(includes));

                DirectoryScanner scanner = new DirectoryScanner();
                scanner.setIncludes(includes);
                scanner.setBasedir(buildDirectory);
                scanner.setCaseSensitive(false);
                scanner.scan();

                String[] includedFiles = scanner.getIncludedFiles();
                getLog().debug("IncludedFiles: " + Arrays.toString(includedFiles));

                for (String includedFile : includedFiles) {
                    File f = new File(buildDirectory + "/" + includedFile);
                    
                    while (!f.getAbsolutePath().equals(buildDirectory)) {
                        if (f.isDirectory()) {
                            String replace = f.getAbsolutePath().replace(buildDirectory + "/", "").replace("/", "\\\\");
                            replace = "\tRMDir \"\\$INSTDIR\\\\" + replace + "\"\n";
                            if (!removeDirs.contains(replace)) {
                                removeDirs.add(replace);
                            }
                        } else if (!fullInstall.contains(includedFile)) {
                            String str = includedFile.replace("/", "\\\\");
                            fullInstall.add("\tFile /r \"" + str + "\"\n");
                            deleteFiles.add("\tDelete \"\\$INSTDIR\\\\" + str + "\"\n");
                        }
                        f = f.getParentFile();
                    }
                }
            }

            templateContent = replace(templateContent, "@NSIS_COMPRESSOR@", compressor.toString());
            templateContent = replace(templateContent, "@NSIS_COMPRESSOR_DIC_SIZE@", "" + compressor.getDictionarySize());
            templateContent = replace(templateContent, "@NSIS_CONTACT@", contact);
            templateContent = replace(templateContent, "@NSIS_DELETE_FILES@", deleteFiles.toString());
            templateContent = replace(templateContent, "@NSIS_DELETE_DIRECTORIES@", removeDirs.toString());
            templateContent = replace(templateContent, "@NSIS_DISPLAY_NAME@", displayName);
            templateContent = replace(templateContent, "@NSIS_DOWNLOAD_SITE@", "");
            templateContent = replace(templateContent, "@NSIS_ENABLE_UNINSTALL_BEFORE_INSTALL@", enableUninstallBeforeInstall ? "ON" : "OFF");
            templateContent = replace(templateContent, "@NSIS_FULL_INSTALL@", fullInstall.toString());
            templateContent = replace(templateContent, "@NSIS_HELP_LINK@", helpLink);
            templateContent = replace(templateContent, "@NSIS_INSTALL_DIRECTORY@", installDirectory.replace("/", "\\\\"));
            templateContent = replace(templateContent, "@NSIS_INSTALL_OPTIONS@", installOptions.getName());
            templateContent = replace(templateContent, "@NSIS_INSTALL_ROOT@", installRoot);
            templateContent = replace(templateContent, "@NSIS_INSTALLER_MUI_COMPONENTS_DESC@", "!define MUI_COMPONENTSPAGE_NODESC");
            templateContent = replace(templateContent, "@NSIS_MODIFY_PATH@", modifyPath ? "ON" : "OFF");
            templateContent = replace(templateContent, "@NSIS_MUI_HEADERIMAGE_BITMAP@", "!define MUI_HEADERIMAGE_BITMAP " + muiHeader);
            templateContent = replace(templateContent, "@NSIS_MUI_ICON@", "!define MUI_ICON " + muiIcon);
            templateContent = replace(templateContent, "@NSIS_MUI_UNICON@", "!define MUI_UNICON " + muiUnIcon);
            templateContent = replace(templateContent, "@NSIS_MUI_WELCOMEFINISHPAGE_BITMAP@", "!define MUI_WELCOMEFINISHPAGE_BITMAP " + muiPanel);
            templateContent = replace(templateContent, "@NSIS_OUTPUT_FILE_NAME@", outputFileName);
            templateContent = replace(templateContent, "@NSIS_PACKAGE_INSTALL_REGISTRY_KEY@", displayName);
            templateContent = replace(templateContent, "@NSIS_PACKAGE_NAME@", packageName);
            templateContent = replace(templateContent, "@NSIS_PACKAGE_VENDOR@", packageVendor);
            templateContent = replace(templateContent, "@NSIS_PACKAGE_VERSION@", packageVersion);
            templateContent = replace(templateContent, "@NSIS_RESOURCE_FILE_LICENSE@", licenseMacro);
            templateContent = replace(templateContent, "@NSIS_SPLASH_IMAGE@", splash);
            templateContent = replace(templateContent, "@NSIS_URL_INFO_ABOUT@", urlInfoAbout);

            Files.write(file.toPath(), templateContent.getBytes(charset));
        } catch (IOException ex) {
            throw new MojoExecutionException("Unable to generate project script " + template.getPath(), ex);
        }
    }

    private String replace(String string, String regex, String replacament) {
        if (replacament == null) {
            replacament = "";
        }
        getLog().debug("Replacing " + regex + " with " + replacament);
        return string.replaceAll(regex, replacament);
    }

}
