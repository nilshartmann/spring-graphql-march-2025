package nh.springgraphql.remoteservice.publisher;

import nh.springgraphql.remoteservice.common.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@RestController
@RequestMapping("/api/publishers")
class PublisherRestApiController {
    private static final Logger logger = LoggerFactory.getLogger(PublisherRestApiController.class);

    private final Publishers publishers = new Publishers();

    @GetMapping("{publisherId}")
    Publisher getPublisher(@PathVariable String publisherId) {
        logger.info("Get publisher with id '{}'", publisherId);
        return publishers.findPublisherWithId(publisherId)
            .orElseThrow(() -> new ResourceNotFoundException("Publisher '%s' not found".formatted(publisherId)));
    }

//    @GetMapping(value = "find/{userIds}")
//    List<Publisher> users(@PathVariable String[] userIds) {
//        logger.info("Finding users with ids '{}'", Arrays.asList(userIds));
//        return users.findUsersWithId(userIds);
//    }
}
