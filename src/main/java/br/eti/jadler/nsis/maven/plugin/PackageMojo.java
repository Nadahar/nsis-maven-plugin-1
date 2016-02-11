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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = false)
public class PackageMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "nsis.makensis", defaultValue = "makensis")
    private String makensis;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final List<String> commands = new ArrayList<>();
            commands.add(makensis);
            commands.add("project.nsi");

            final ProcessBuilder builder = new ProcessBuilder(commands);
            final String directory = project.getBuild().getDirectory();
            builder.directory(new File(directory));
            builder.redirectErrorStream(true);

            final Process process = builder.start();

            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            final BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException ex) {
            getLog().warn(ex);
        }
    }

}
