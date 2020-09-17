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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class PodcastPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME_PODCAST = "podcast"
    public static final String TASK_PODCAST_FEED = "podcastFeed"
    public static final String TASK_BUILD = "build"

    @Override
    void apply(Project project) {
        project.getPlugins().apply(BasePlugin.class)

        PodcastExtension extension = project.getExtensions().create(EXTENSION_NAME_PODCAST, PodcastExtension.class, project)
        project.getTasks().register(TASK_PODCAST_FEED, PodcastFeed, new Action<PodcastFeed>() {
            @Override
            void execute(PodcastFeed podcastFeed) {
                podcastFeed.setGroup("podcast")
                podcastFeed.setDescription("Generates a RSS feed for the podcast")
                podcastFeed.getEpisodes().set(extension.episodes)
                podcastFeed.getOutput().set(project.getBuildDir())
                podcastFeed.getAuthor().set(extension.author)
                podcastFeed.getTitle().set(extension.title)
                podcastFeed.getLink().set(extension.link)
                podcastFeed.getCopyright().set(extension.copyright)
                podcastFeed.getLanguage().set(extension.language)
                podcastFeed.getKeywords().set(extension.keywords)
                podcastFeed.getAbout().set(extension.description)
                podcastFeed.getSubtitle().set(extension.subtitle)
                podcastFeed.getType().set(extension.type)
                podcastFeed.getImageTitle().set(extension.image.title)
                podcastFeed.getImageLink().set(extension.image.link)
                podcastFeed.getImageUrl().set(extension.image.url)
                podcastFeed.getImageWidth().set(extension.image.width)
                podcastFeed.getImageHeight().set(extension.image.height)

                podcastFeed.getBlock().set(extension.block)
                podcastFeed.getCategories().set(extension.categories)
                podcastFeed.getExplicit().set(extension.explicit)
                podcastFeed.getOwnerName().set(extension.owner.name)
                podcastFeed.getOwnerEmail().set(extension.owner.email)
            }
        })

        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_PODCAST_FEED)
            }
        })

    }
}
