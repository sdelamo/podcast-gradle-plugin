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

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property

@CompileStatic
class PodcastExtension {
    private final Property<String> author
    private final Owner owner = new Owner()
    private final Image image = new Image()

    private final Property<String> title
    private final Property<String> subtitle
    private final Property<String> link
    private final Property<String> copyright
    private final Property<String> language
    private final Property<String> keywords
    private final Property<String> description
    private final Property<String> type
    private final Property<Boolean> block
    private final Property<String> categories
    private final Property<Boolean> explicit
    private final Property<File> episodes

    PodcastExtension(Project project) {
        episodes = project.getObjects().property(File)

        author = project.getObjects().property(String)
        title = project.getObjects().property(String)
        subtitle = project.getObjects().property(String)
        link = project.getObjects().property(String)
        copyright = project.getObjects().property(String)
        language = project.getObjects().property(String)
        keywords = project.getObjects().property(String)
        description = project.getObjects().property(String)
        type = project.getObjects().property(String)
        block = project.getObjects().property(Boolean)
        categories = project.getObjects().property(String)
        explicit = project.getObjects().property(Boolean)
    }

    Property<File> getEpisodes() {
        this.episodes
    }

    Property<String> getAuthor() {
        this.author
    }

    Owner getOwner() {
        this.owner
    }

    void owner(Action<? super Owner> action) {
        action.execute(owner)
    }

    Image getImage() {
        this.image
    }

    void image(Action<? super Image> action) {
        action.execute(image)
    }

    Property<String> getTitle() {
        this.title
    }

    Property<String> getSubtitle() {
        this.subtitle
    }

    Property<String> getLink() {
        this.link
    }

    Property<String> getCopyright() {
        this.copyright
    }

    Property<String> getLanguage() {
        this.language
    }

    Property<String> getKeywords() {
        this.keywords
    }

    Property<String> getDescription() {
        this.description
    }

    Property<String> getType() {
        this.type
    }

    Property<Boolean> getBlock() {
        this.block
    }

    Property<Boolean> getExplicit() {
        this.explicit
    }

    Property<String> getCategories() {
        this.categories
    }
}
