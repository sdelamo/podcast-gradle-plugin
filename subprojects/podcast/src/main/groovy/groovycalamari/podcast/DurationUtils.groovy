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

final class DurationUtils {

    static String durationFromSeconds(int seconds) {
        int p1 = seconds % 60
        int p2 = (seconds / 60).toInteger()
        int p3 = p2 % 60
        p2 = (p2 / 60).toInteger();
        return "${formatTime(p2)}:${formatTime(p3)}:${formatTime(p1)}"
    }

    private static String formatTime(Integer val) {
        return (""+val).padLeft(2, '0')
    }
}
