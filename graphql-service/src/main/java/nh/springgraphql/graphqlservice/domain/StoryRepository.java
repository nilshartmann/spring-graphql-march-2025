package nh.springgraphql.graphqlservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This simulates some store of stories. To make things simple during the workshop everything we need is
 * placed in this single class.
 * <p>
 * In real life, that would be a database, remote service, ... <br/>
 * Also it would maybe be splitted into multiple parts: Stories, Comments, ExcerptService, ...
 * </p>
 * <p>
 * This is <b>not threadsafe</b>. While you can access the repository with multiple read requests in parallel,
 * writing (add new comments) might fail
 * </p>
 */
@Component
public class StoryRepository {

    private final List<Story> stories;
    @Autowired
    private ApplicationEventPublisher publisher;

    public StoryRepository() {
        this.stories = generateStories();
    }

    public List<Story> findAllStories() {
        return stories.stream().toList();
    }

    public Optional<Story> findStory(String storyId) {
        return stories.stream().filter(s -> s.id().equals(storyId)).findAny();
    }

    public Optional<Story> findStoryForComment(String commentId) {
        for (Story s : stories) {
            if (s.comments().stream().anyMatch(c -> c.id().equals(commentId))) {
                return Optional.of(s);
            }
        }

        return Optional.empty();
    }


    public String generateExcerpt(Story story, int maxLength) {
        var b = story.body();
        return b.substring(0, Math.min(maxLength, b.length() - 1));
    }

    public Optional<Comment> findComment(String id) {
        return this.stories.stream()
            .flatMap(s -> s.comments().stream().filter(c -> c.id().equals(id)))
            .findAny();
    }

    public List<Comment> findCommentsForStory(String storyId) {
        var story = findStory(storyId).orElseThrow(
            () -> new ResourceNotFoundException("Story '%s' not found".formatted(storyId))
        );

        return story.comments().stream().toList();
    }

    public Comment addComment(String storyId, String text, int rating) {
        var story = findStory(storyId).orElseThrow(
            () -> new ResourceNotFoundException("Story '%s' not found".formatted(storyId))
        );

        var newComment = new Comment(
            newCommentId(),
            text,
            rating
        );

        story.comments().add(newComment);

        publisher.publishEvent(
            new CommentCreatedEvent(newComment)
        );

        return newComment;
    }

    private String newCommentId() {
        //  😈 😈 😈 😈
        return "" + this.stories.stream().mapToInt(s -> s.comments().size()).sum();
    }

//    public static void main(String[] args) {
//        StoryRepository repo = new StoryRepository();
//        var stories = repo.stories;
//
//        // Print each story and its comments
//        for (Story story : stories) {
//            System.out.println("Story: " + story);
//            System.out.println("Comments: " + repo.findCommentsForStory(story.id()));
//            System.out.println();
//        }
//
//        // Example: Add a comment to a story in a thread-safe manner
////        repo.addComment("1", new Comment("13", "Looking forward to more updates!", 4));
//
//        // Print updated comments for Story 1
    ////        System.out.println("Updated Comments for Story 1: " + repo.getComments("1"));
//    }

    private static List<Story> generateStories() {
        return List.of(
            new Story(
                "1",
                OffsetDateTime.of(2024, 11, 20, 14, 30, 0, 0, ZoneOffset.ofHours(2)),
                "Tech Innovations in 2024",
                "The year 2024 is shaping up to be a pivotal moment for technology, with breakthroughs in artificial intelligence, robotics, and space exploration taking center stage. AI is not just a tool but is rapidly becoming an indispensable part of our daily lives, automating complex tasks and enabling more personalized user experiences. Meanwhile, space exploration is pushing the boundaries of what humanity can achieve, with private companies and governments working together on missions to the Moon and beyond.",
                "1",
                PublicationState.published,
                new LinkedList<>(List.of(
                    new Comment("1", "This is so exciting!", 5),
                    new Comment("2", "AI is really changing the game.", 3)
                ))
            ),
            new Story(
                "2",
                OffsetDateTime.of(2024, 11, 21, 10, 15, 0, 0, ZoneOffset.ofHours(1)),
                "Global Climate Update",
                "As the effects of climate change become more apparent, 2024 has seen a surge in global awareness and action. Renewable energy sources such as wind and solar are gaining traction, providing hope for a greener future. However, challenges remain as industrial giants and political conflicts slow progress. This update explores the latest scientific findings, policy changes, and community-driven efforts to combat climate change.",
                "5",
                PublicationState.published,
                new LinkedList<>(List.of(
                    new Comment("3", "Scary but informative.", 2),
                    new Comment("4", "We need to act now.", 3),
                    new Comment("5", "Great coverage!", 2)
                ))
            ),
            new Story(
                "3",
                OffsetDateTime.of(2024, 11, 22, 9, 0, 0, 0, ZoneOffset.ofHours(2)),
                "The Future of Work",
                "With remote work now firmly established, 2024 marks a new era of workplace evolution. Companies are embracing hybrid models, blending the flexibility of remote setups with the collaborative energy of in-person interactions. Automation and AI are revolutionizing industries, creating demand for new, specialized skills. Organizations are redefining what it means to work in a post-pandemic world.",
                "3",
                PublicationState.in_review,
                new LinkedList<>(List.of(
                    new Comment("6", "Working remotely has changed my life.", 5),
                    new Comment("7", "Excited about hybrid models.", 4),
                    new Comment("8", "Automation sounds promising.", 5)
                ))
            ),
            new Story(
                "4",
                OffsetDateTime.of(2024, 11, 23, 16, 45, 0, 0, ZoneOffset.ofHours(1)),
                "Breakthroughs in Medicine",
                "Medical science continues to push boundaries in 2024, with groundbreaking discoveries that promise to change the face of healthcare. Advances in personalized medicine are enabling doctors to tailor treatments to individual patients, increasing efficacy and reducing side effects. In parallel, telemedicine is expanding access to healthcare, breaking down barriers for rural and underserved communities.",
                "4",
                PublicationState.published,
                new LinkedList<>(List.of(
                    new Comment("9", "Medicine is advancing so fast.", 2),
                    new Comment("10", "Telemedicine has been a lifesaver.", 4),
                    new Comment("11", "Exciting breakthroughs!", 4)
                ))
            ),
            new Story(
                "5",
                OffsetDateTime.of(2024, 11, 24, 13, 19, 0, 0, ZoneOffset.ofHours(2)),
                "Art in the Digital Age",
                "Art is undergoing a renaissance in 2024, fueled by digital tools and platforms that are transforming how we create, consume, and value artistic expression. AI-generated art is now a respected medium, blending creativity with technology. Traditional artists are embracing these tools to enhance their crafts, creating a dynamic interplay between the old and the new.",
                "2",
                PublicationState.draft,
                new LinkedList<>(List.of(
                    new Comment("12", "Digital art is fascinating.", 4),
                    new Comment("13", "AI art is the future.", 4),
                    new Comment("14", "Traditional art still holds value.", 4)
                ))
            ),
            new Story(
                "6",
                OffsetDateTime.of(2024, 11, 25, 11, 32, 0, 0, ZoneOffset.ofHours(3)),
                "Space Exploration Milestones",
                "In 2024, space exploration is reaching new heights as both private companies and governments push forward ambitious missions. The deployment of new telescopes has allowed scientists to uncover secrets about distant planets and galaxies. Meanwhile, lunar and Martian exploration programs continue to lay the groundwork for human colonization.",
                "4",
                PublicationState.draft,
                new LinkedList<>(List.of(
                    new Comment("15", "Mars missions are incredible.", 4),
                    new Comment("16", "Space exploration inspires us.", 5)
                ))
            ),
            new Story(
                "7",
                OffsetDateTime.of(2024, 11, 26, 8, 12, 0, 0, ZoneOffset.ofHours(2)),
                "Renewable Energy Revolution",
                "The global push for renewable energy has reached unprecedented levels in 2024. Solar and wind technologies have become more efficient and affordable than ever. Governments worldwide are rolling out large-scale projects to reduce dependence on fossil fuels. These efforts are creating jobs and driving innovation in the energy sector.",
                "2",
                PublicationState.in_review,
                new LinkedList<>(List.of(
                    new Comment("17", "Renewable energy is vital.", 2),
                    new Comment("18", "Solar power is amazing.", 4),
                    new Comment("19", "Great steps for sustainability.", 2)
                ))
            ),
            new Story(
                "8",
                OffsetDateTime.of(2024, 11, 27, 14, 17, 0, 0, ZoneOffset.ofHours(2)),
                "Rise of Electric Vehicles",
                "The electric vehicle industry is booming in 2024, with new models offering greater range, affordability, and sustainability. Governments are incentivizing EV adoption with tax breaks and infrastructure improvements. Battery technology has seen remarkable progress, reducing charging times and enhancing energy storage capabilities.",
                "5",
                PublicationState.published,
                new LinkedList<>(List.of(
                    new Comment("20", "EVs are the future.", 5),
                    new Comment("21", "Charging networks are growing fast.", 4),
                    new Comment("22", "Can't wait to buy an EV!", 5)
                ))
            )
        );
    }

}