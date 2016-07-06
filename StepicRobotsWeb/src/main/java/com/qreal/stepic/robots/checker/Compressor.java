/*
 * Copyright Vladimir Zakharov
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

package com.qreal.stepic.robots.checker;

import com.qreal.stepic.robots.constants.PathConstants;

import java.io.*;

/**
 * Created by vladimir-zakharov on 31.08.15.
 */
public class Compressor {

    public void compress(String taskId, String pathToFolder) throws IOException, InterruptedException {
        File folder = new File(pathToFolder);
        ProcessBuilder compressorProcBuilder = new ProcessBuilder(PathConstants.COMPRESSOR_PATH, taskId);
        compressorProcBuilder.directory(folder);
        compressorProcBuilder.start().waitFor();
    }

    public void decompress(String kit, String taskId) throws IOException, InterruptedException {
        String pathToFile = String.format("%s/trikKit%s/tasks/%s", PathConstants.STEPIC_PATH, kit, taskId);
        File folder = new File(pathToFile);
        File diagramDirectory = new File(pathToFile + "/" + taskId);

        if (!diagramDirectory.exists()) {
            ProcessBuilder processBuilder = new ProcessBuilder(PathConstants.COMPRESSOR_PATH, taskId + ".qrs");
            processBuilder.directory(folder);

            final Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        }
    }

}
