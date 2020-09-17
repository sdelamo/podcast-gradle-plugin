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

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet
import groovy.transform.CompileStatic

@CompileStatic
class MarkdownUtil {
    static DataHolder OPTIONS = new MutableDataSet().toImmutable()
    static Parser PARSER = Parser.builder(OPTIONS).build()
    static HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build()

    static String htmlFromMarkdown(String markdown) {
        Node document = PARSER.parse(markdown)
        RENDERER.render(document)
    }
}
