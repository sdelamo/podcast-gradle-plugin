/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovycalamari.podcast

import com.mpatric.mp3agic.Mp3File

final class Mp3FileUtils {

    static Mp3File mp3FileFromUrl(String enclosureUrl) {
        return new Mp3File(fileFromUrl(enclosureUrl))
    }

    private static File fileFromUrl(String urlString) {
        URL url = new URL(urlString)
        URLConnection connection = url.openConnection()
        InputStream inputStream = connection.getInputStream()
        File file  = File.createTempFile('enclosure', '.mp3')
        file.deleteOnExit()
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[512]
        while (true) {
            int len = inputStream.read(buf)
            if (len == -1) {
                break
            }
            fos.write(buf, 0, len)
        }
        inputStream.close()
        fos.flush()
        fos.close()
        file
    }
}
