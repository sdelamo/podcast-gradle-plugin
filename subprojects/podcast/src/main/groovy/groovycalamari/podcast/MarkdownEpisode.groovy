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
import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.rss.itunespodcast.ItunesPodcastEpisodeType
import org.jetbrains.annotations.Nullable

@ToString
@CompileStatic
class MarkdownEpisode {
    private static final String KEY_TITLE = 'title'
    private static final String KEY_AUTHOR = 'author'
    private static final String KEY_EPISODE_TYPE = 'episodeType'
    private static final String KEY_SUBTITLE = 'subtitle'
    private static final String KEY_SUMMARY = 'summary'
    private static final String KEY_DESCRIPTION = 'description'
    private static final String KEY_ENCLOSURE_URL = 'enclosureUrl'
    private static final String KEY_ENCLOSURE_TYPE = 'enclosureType'
    private static final String KEY_ENCLOSURE_LENGTH = 'enclosureLength'
    private static final String KEY_GUID = 'guid'
    private static final String KEY_DURATION = 'duration'
    private static final String KEY_IMAGE = 'image'
    private static final String KEY_EPISODE = 'episode'
    private static final String KEY_SEASON = 'season'
    private static final String KEY_EXPLICIT = 'explicit'
    private static final String KEY_PUB_DATE = 'pubDate'

    String filename
    Map<String, String> metadata
    String content
    Mp3File mp3File

    String getPubDate() {
        metadata[KEY_PUB_DATE]
    }

    Integer getEpisode() {
        metadata[KEY_EPISODE] ? Integer.valueOf(metadata[KEY_EPISODE]) : null
    }

    Integer getSeason() {
        metadata[KEY_SEASON] ? Integer.valueOf(metadata[KEY_SEASON]) : null
    }

    Boolean getExplicit() {
        metadata[KEY_EXPLICIT] ? Boolean.valueOf(metadata[KEY_EXPLICIT]) : null
    }

    String getDuration() {
        String duration = metadata[KEY_DURATION]
        if (duration) {
            return duration
        }
        Mp3File mp3 = getMp3File()
        if (mp3) {
            return DurationUtils.durationFromSeconds(mp3.getLengthInSeconds().toInteger())
        }
        null
    }

    String getImage() {
        metadata[KEY_IMAGE]
    }

    String getGuid() {
        metadata[KEY_GUID]
    }

    String getTitle() {
        metadata[KEY_TITLE]
    }

    String getAuthor() {
        metadata[KEY_AUTHOR]
    }

    String getSubtitle() {
        metadata[KEY_SUBTITLE]
    }

    String getSummary() {
        metadata[KEY_SUMMARY]
    }

    String getDescription() {
        metadata[KEY_DESCRIPTION]
    }

    ItunesPodcastEpisodeType getEpisodeType() {
        metadata[KEY_EPISODE_TYPE] ?  metadata[KEY_EPISODE_TYPE]?.toUpperCase() as ItunesPodcastEpisodeType : null

    }

    @Nullable
    Mp3File getMp3File() {
        if (this.mp3File != null) {
            return this.mp3File
        }
        if (enclosureUrl) {
            this.mp3File = Mp3FileUtils.mp3FileFromUrl(enclosureUrl)
            return this.mp3File
        }
        null
    }

    @Nullable
    Integer getEnclosureLength() {
        if (metadata[KEY_ENCLOSURE_LENGTH]) {
            return Integer.valueOf(metadata[KEY_ENCLOSURE_LENGTH])
        }
        Mp3File mp3 = mp3File
        if (mp3) {
            return mp3.getLength()
        }
        null
    }

    @Nullable
    String getEnclosureUrl() {
        metadata[KEY_ENCLOSURE_URL]
    }

    Enclosure getEnclosure() {
        new Enclosure(url: enclosureUrl,
                type: metadata[KEY_ENCLOSURE_TYPE],
                length: enclosureLength)
    }
}

