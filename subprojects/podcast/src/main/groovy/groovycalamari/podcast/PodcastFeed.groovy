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
import io.micronaut.rss.RssChannelImage
import io.micronaut.rss.RssFeedRenderer
import io.micronaut.rss.RssItemEnclosure
import io.micronaut.rss.RssLanguage
import io.micronaut.rss.itunespodcast.DefaultItunesPodcastRenderer
import io.micronaut.rss.itunespodcast.ItunesPodcastCategory
import io.micronaut.rss.itunespodcast.ItunesPodcastEpisode
import io.micronaut.rss.itunespodcast.ItunesPodcastOwner
import io.micronaut.rss.itunespodcast.ItunesPodcastType
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import io.micronaut.rss.itunespodcast.ItunesPodcast

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@CompileStatic
class PodcastFeed extends DefaultTask {
    static DateFormat JSON_FEED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    static {
        TimeZone tz = TimeZone.getTimeZone("Europe/Madrid")
        JSON_FEED_FORMAT.setTimeZone(tz)
    }
    static final String COLON = ":"
    static final String SEPARATOR = "---"
    public static final String SUFFIX_MD = ".md"
    public static final String SUFFIX_MARKDOWN = ".markdown"
    public static final String RSS_FILE = 'rss.xml'
    public static final String COMMA = ","

    private final Property<String> ownerName
    private final Property<String> ownerEmail
    private final Property<String> author

    private final Property<String> title
    private final Property<String> link
    private final Property<String> copyright
    private final Property<String> language
    private final Property<String> keywords
    private final Property<String> about
    private final Property<String> subtitle
    private final Property<String> type
    private final Property<String> imageTitle
    private final Property<String> imageUrl
    private final Property<String> imageLink
    private final Property<Integer> imageHeight
    private final Property<Integer> imageWidth
    private final Property<Boolean> block
    private final Property<String> categories
    private final Property<Boolean> explicit
    private final Property<File> episodes
    private final Property<File> output

    PodcastFeed() {
        episodes = getProject().getObjects().property(File)
        output = getProject().getObjects().property(File)

        ownerName = getProject().getObjects().property(String)
        ownerEmail = getProject().getObjects().property(String)
        author = getProject().getObjects().property(String)

        title = getProject().getObjects().property(String)
        link = getProject().getObjects().property(String)
        copyright = getProject().getObjects().property(String)
        language = getProject().getObjects().property(String)
        keywords = getProject().getObjects().property(String)
        about = getProject().getObjects().property(String)

        subtitle = getProject().getObjects().property(String)
        type = getProject().getObjects().property(String)

        imageUrl = getProject().getObjects().property(String)
        imageLink = getProject().getObjects().property(String)
        imageTitle = getProject().getObjects().property(String)
        imageWidth = getProject().getObjects().property(Integer)
        imageHeight = getProject().getObjects().property(Integer)

        block = getProject().getObjects().property(Boolean)
        categories = getProject().getObjects().property(String)
        explicit = getProject().getObjects().property(Boolean)
    }

    @Input
    Property<String> getTitle() {
        return title
    }

    @Input
    Property<String> getLink() {
        return link
    }

    @Input
    Property<String> getCopyright() {
        return copyright
    }

    @Input
    Property<String> getLanguage() {
        return language
    }

    @Optional
    @Input
    Property<String> getKeywords() {
        return keywords
    }

    @Input
    Property<String> getAbout() {
        return about
    }

    @Optional
    @Input
    Property<String> getSubtitle() {
        return subtitle
    }

    @Input
    Property<String> getType() {
        return type
    }

    @Input
    Property<String> getImageUrl() {
        return imageUrl
    }

    @Optional
    @Input
    Property<Integer> getImageWidth() {
        return imageWidth
    }

    @Optional
    @Input
    Property<Integer> getImageHeight() {
        return imageHeight
    }

    @Optional
    @Input
    Property<String> getImageTitle() {
        return imageTitle
    }

    @Optional
    @Input
    Property<String> getImageLink() {
        return imageLink
    }

    @Optional
    @Input
    Property<Boolean> getBlock() {
        return block
    }

    @Input
    Property<String> getCategories() {
        return categories
    }

    @Input
    Property<Boolean> getExplicit() {
        return explicit
    }

    @Input
    Property<String> getAuthor() {
        return author
    }

    @Input
    Property<String> getOwnerName() {
        return ownerName
    }

    @Input
    Property<String> getOwnerEmail() {
        return ownerEmail
    }

    @InputDirectory
    Property<File> getEpisodes() {
        return episodes
    }

    @OutputDirectory
    Property<File> getOutput() {
        return output
    }

    @TaskAction
    void generatePodcastFeed() {
        ItunesPodcast itunesPodcast = itunesPodcast()
        File outputFile = new File(output.get().absolutePath + File.separator + RSS_FILE)
        FileWriter writer = new FileWriter(outputFile)
        RssFeedRenderer rssFeedRenderer = new DefaultItunesPodcastRenderer()
        rssFeedRenderer.render(writer, itunesPodcast)
        writer.close()
    }

    private ItunesPodcast itunesPodcast() {
        ItunesPodcast.Builder podcastBuilder = iTunesPodcastBuilder()

        List<MarkdownEpisode> episodes = parseEpisodes(episodes.get())
        episodes.each {
            ItunesPodcastEpisode episode = episodeFromMarkdown(it)
            podcastBuilder.item(episode)
        }

        podcastBuilder.build()
    }

    private ItunesPodcastEpisode episodeFromMarkdown(MarkdownEpisode markdownEpisode) {
        ItunesPodcastEpisode.Builder builder = ItunesPodcastEpisode.builder(markdownEpisode.title)
        if (markdownEpisode.episodeType) {
            builder = builder.episodeType(markdownEpisode.episodeType)
        }
        if (markdownEpisode.guid) {
            builder = builder.guid(markdownEpisode.guid)
        }
        if (markdownEpisode.duration) {
            builder = builder.duration(markdownEpisode.duration)
        }
        if (markdownEpisode.image) {
            builder = builder.image(markdownEpisode.image)
        }
        if (markdownEpisode.episode) {
            builder = builder.episode(markdownEpisode.episode)
        }
        if (markdownEpisode.season) {
            builder = builder.season(markdownEpisode.season)
        }
        if (markdownEpisode.explicit) {
            builder = builder.explicit(markdownEpisode.explicit)
        }
        if (markdownEpisode.author) {
            builder = builder.author(markdownEpisode.author)
        }
        if (markdownEpisode.pubDate) {
            Date pubDate = parseDate(markdownEpisode.pubDate)
            builder = builder.pubDate(ZonedDateTime.of(Instant.ofEpochMilli(pubDate.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime(), ZoneId.of("GMT")))
        }
        if (markdownEpisode.subtitle) {
            builder = builder.subtitle(markdownEpisode.subtitle)
        }
        if (markdownEpisode.summary) {
            builder = builder.summary(markdownEpisode.summary)
        }
        if (markdownEpisode.content) {
            builder = builder.contentEncoded(MarkdownUtil.htmlFromMarkdown(markdownEpisode.content))
        }

        RssItemEnclosure.Builder enclosureBuilder = RssItemEnclosure.builder()
        Enclosure enclosure = markdownEpisode.enclosure
        if (!enclosure.type) {
            throw new GradleException("Enclosure type for ${markdownEpisode.filename} not present")
        }
        if (!enclosure.url) {
            throw new GradleException("Enclosure url for ${markdownEpisode.filename} not present")
        }
        if (!enclosure.length) {
            throw new GradleException("Enclosure length for ${markdownEpisode.filename} not present")
        }
        if (enclosure.url) {
            enclosureBuilder.url(enclosure.url)
        }
        if (enclosure.type) {
            enclosureBuilder.type(enclosure.type)
        }
        if (enclosure.length) {
            enclosureBuilder.length(enclosure.length)
        }
        builder.enclosure(enclosureBuilder.build())
        builder.build()
    }

    private ItunesPodcast.Builder iTunesPodcastBuilder() {
            ItunesPodcast.Builder podcastBuilder = ItunesPodcast.builder()

        if (!title.isPresent()) {
            throw new GradleException("podcast.title is required")
        }
        podcastBuilder.title(title.get())

        if (!link.isPresent()) {
            throw new GradleException("podcast.link is required")
        }
        podcastBuilder.link(link.get())

        if (!copyright.isPresent()) {
            throw new GradleException("podcast.copyright is required")
        }
        podcastBuilder.copyright(copyright.get())

        podcastBuilder.language(rssLanguage())
        if (keywords.isPresent()) {
            for (String keyword : keywords.get().split(COMMA)) {
                podcastBuilder.keyword(keyword)
            }
        }
        if (!about.isPresent()) {
            throw new GradleException("podcast.description is required")
        }
        podcastBuilder.description(about.get())

        if (subtitle.isPresent()) {
            podcastBuilder.subtitle(subtitle.get())
        }

        if (!author.isPresent()) {
            throw new GradleException("podcast.author is required")
        }
        podcastBuilder.author(author.get())

        podcastBuilder.type(itunesPodcastType())

        podcastBuilder.owner(owner())
        podcastBuilder.image(rssChannelImage())
        podcastBuilder.block(block.isPresent() ? block.get() : false)

        if (!explicit.isPresent()) {
            throw new GradleException("podcast.explicit is required")
        }
        podcastBuilder.explict(explicit.get())

        podcastBuilder.category([(categories.get() as ItunesPodcastCategory).getCategories()])

        podcastBuilder
    }

    private ItunesPodcastType itunesPodcastType() {
        if (!type.isPresent()) {
            throw new GradleException("podcast.type is required")
        }

        String value = type.get()

        ItunesPodcastType itunesPodcastType = ItunesPodcastType.values().find {it.name().equalsIgnoreCase(value) }
        if (itunesPodcastType == null) {
            throw new GradleException("podcast.type must be " + ItunesPodcastType.values().join(" or "))
        }
        itunesPodcastType
    }

    private ItunesPodcastOwner owner() {
        ItunesPodcastOwner.Builder builder = ItunesPodcastOwner.builder()
        String email = ownerEmail.get()
        builder.email(email)

        String name = ownerName.get()
        builder.name(name)

        builder.build()
    }

    private RssLanguage rssLanguage() {
        if (!language.isPresent()) {
            throw new GradleException("podcast.language is required")
        }
        String languageCode = language.get()

        RssLanguage rssLanguage = RssLanguage.values().find {lang -> (lang.languageCode == languageCode) }
        if (rssLanguage == null) {
            throw new GradleException("language code " + languageCode + " must any of " + RssLanguage.values().collect { it.languageCode }.join(","))
        }
        rssLanguage
    }

    private RssChannelImage rssChannelImage() {
        if (!imageUrl.isPresent()) {
            throw new GradleException("podcast.image.url is required")
        }
        String url = imageUrl.get()

        String link = imageLink.isPresent() ? imageLink.get() : link.get()

        String title = imageTitle.isPresent() ? imageTitle.get() : title.get()
        RssChannelImage.Builder builder = RssChannelImage.builder(title, url, link)
        if (imageWidth.isPresent()) {
            builder.width(imageWidth.get())
        }
        if (imageHeight.isPresent()) {
            builder.width(imageHeight.get())
        }
        builder.build()
    }

    List<MarkdownEpisode> parseEpisodes(File folder) {
        List<MarkdownEpisode> listOfEpisodes = []
        folder.eachFile { file ->
            if (file.path.endsWith(SUFFIX_MD) || file.path.endsWith(SUFFIX_MARKDOWN)) {
                ContentAndMetadata cm = parseFile(file)
                listOfEpisodes << new MarkdownEpisode(filename: file.name, content: cm.content, metadata: cm.metadata)
            }
        }
        listOfEpisodes
    }

    static ContentAndMetadata parseFile(File file) {
        String line = null
        List<String> lines = []
        Map<String, String> metadata = [:]
        boolean metadataProcessed = false

        int lineCount = 0
        file.withReader { reader ->
            while ((line = reader.readLine()) != null) {
                if (lineCount == 0 && line.startsWith(SEPARATOR)) {
                    continue
                }
                lineCount++
                if (!metadataProcessed && line.startsWith(SEPARATOR)) {
                    metadataProcessed = true
                    continue
                }
                if (!metadataProcessed && line.contains(COLON)) {
                    String metadataKey = line.substring(0, line.indexOf(COLON as String)).trim()
                    String metadataValue = line.substring(line.indexOf(COLON as String) + COLON.length()).trim()
                    metadata[metadataKey] = metadataValue
                }
                if (metadataProcessed) {
                    lines << line
                }
            }
        }
        boolean empty = (!metadataProcessed || lines.isEmpty())
        new ContentAndMetadata(metadata: empty ? ([:] as Map<String, String>) : metadata,
                content: empty ? file.text : lines.join("\n"))
    }


    static Date parseDate(String date) throws ParseException {
        if (!date) {
            throw new GradleException("Could not parse date $date")
        }
        try {
            return JSON_FEED_FORMAT.parse(date)
        } catch(ParseException e) {
            throw new GradleException("Could not parse date $date")
        }
    }


}
