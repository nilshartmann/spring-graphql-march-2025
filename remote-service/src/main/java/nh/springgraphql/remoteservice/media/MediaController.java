package nh.springgraphql.remoteservice.media;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
class MediaController {
    private final List<Media> content = generateMediaData();

    @DgsQuery
    public List<Media> media(@InputArgument String storyId) {
        return content.stream()
            .filter(m -> m.storyId().equals(storyId))
            .toList();
    }

    enum MediaType {
        image,
        video,
        audio
    }

    record MediaMetadata(
        String dimensions,
        Integer duration,
        String codec
    ) {}

    record Media(
        String storyId,
        String id,
        String title,
        String url,
        MediaType type,
        MediaMetadata metadata
    ) {}

    private static List<Media> generateMediaData() {
        MediaMetadata imageMetadata = new MediaMetadata("1920x1080", null, null);
        MediaMetadata videoMetadata = new MediaMetadata("1920x1080", 120, "H.264");
        MediaMetadata audioMetadata = new MediaMetadata(null, 300, "MP3");

        return List.of(
            // Story 1
            new Media("1", "m11", "Sunset Over the Mountains", "https://example.com/story1/sunset.jpg", MediaType.image, imageMetadata),
            new Media("1", "m12", "Mountain Climbing Documentary", "https://example.com/story1/climbing.mp4", MediaType.video, videoMetadata),
            new Media("1", "m13", "Relaxing Mountain Ambience", "https://example.com/story1/mountain-audio.mp3", MediaType.audio, audioMetadata),

            // Story 2
            new Media("2", "m21", "City Skyline at Night", "https://example.com/story2/skyline.jpg", MediaType.image, imageMetadata),
            new Media("2", "m22", "Nightlife Vlog", "https://example.com/story2/nightlife.mp4", MediaType.video, videoMetadata),
            new Media("2", "m23", "City Street Sounds", "https://example.com/story2/city-audio.mp3", MediaType.audio, audioMetadata),

            // Story 3
            new Media("3", "m31", "A Beautiful Forest Path", "https://example.com/story3/forest.jpg", MediaType.image, imageMetadata),
            new Media("3", "m32", "Walking Through the Forest", "https://example.com/story3/walk.mp4", MediaType.video, videoMetadata),
            new Media("3", "m33", "Birds Chirping and Rustling Leaves", "https://example.com/story3/forest-audio.mp3", MediaType.audio, audioMetadata),

            // Story 4
            new Media("4", "m41", "Golden Sand Dunes", "https://example.com/story4/dunes.jpg", MediaType.image, imageMetadata),
            new Media("4", "m42", "Desert Safari Adventure", "https://example.com/story4/safari.mp4", MediaType.video, videoMetadata),
            new Media("4", "m43", "Desert Winds Soundscape", "https://example.com/story4/desert-audio.mp3", MediaType.audio, audioMetadata),

            // Story 5
            new Media("5", "m51", "Snow-Capped Peaks", "https://example.com/story5/snow.jpg", MediaType.image, imageMetadata),
            new Media("5", "m52", "Snowboarding Highlights", "https://example.com/story5/snowboarding.mp4", MediaType.video, videoMetadata),
            new Media("5", "m53", "Winter Wonderland Music", "https://example.com/story5/snow-audio.mp3", MediaType.audio, audioMetadata),

            // Story 6
            new Media("6", "m61", "Tropical Beach Paradise", "https://example.com/story6/beach.jpg", MediaType.image, imageMetadata),
            new Media("6", "m62", "Surfing Adventures", "https://example.com/story6/surfing.mp4", MediaType.video, videoMetadata),
            new Media("6", "m63", "Ocean Waves Relaxation", "https://example.com/story6/waves.mp3", MediaType.audio, audioMetadata),

            // Story 7
            new Media("7", "m71", "A Colorful Garden", "https://example.com/story7/garden.jpg", MediaType.image, imageMetadata),
            new Media("7", "m72", "Time-Lapse of Blooming Flowers", "https://example.com/story7/blooming.mp4", MediaType.video, videoMetadata),
            new Media("7", "m73", "Garden Ambience with Birds", "https://example.com/story7/garden-audio.mp3", MediaType.audio, audioMetadata),

            // Story 8
            new Media("8", "m81", "Historical Castle View", "https://example.com/story8/castle.jpg", MediaType.image, imageMetadata),
            new Media("8", "m82", "Castle Tour Highlights", "https://example.com/story8/castle-tour.mp4", MediaType.video, videoMetadata),
            new Media("8", "m83", "Medieval Music", "https://example.com/story8/medieval.mp3", MediaType.audio, audioMetadata)
        );

    }


}
