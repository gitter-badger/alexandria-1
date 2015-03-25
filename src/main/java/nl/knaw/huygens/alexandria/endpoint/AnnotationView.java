package nl.knaw.huygens.alexandria.endpoint;

import static nl.knaw.huygens.alexandria.endpoint.Annotations.ANNOTATIONS_PATH;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.Sets;
import nl.knaw.huygens.alexandria.config.AlexandriaConfiguration;
import nl.knaw.huygens.alexandria.model.AlexandriaAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonTypeName("annotation")
public class AnnotationView {
  private static final Logger LOG = LoggerFactory.getLogger(AnnotationView.class);

  @JsonIgnore
  private final AlexandriaAnnotation annotation;

  @JsonIgnore
  private AlexandriaConfiguration config;

  public static final AnnotationView of(AlexandriaAnnotation someAnnotation) {
    return new AnnotationView(someAnnotation);
  }

  private AnnotationView(AlexandriaAnnotation annotation) {
    this.annotation = annotation;
  }

  public final AnnotationView withConfig(AlexandriaConfiguration config) {
    this.config = config;
    return this;
  }

  public UUID getId() {
    return annotation.getId();
  }

  public String getType() {
    return annotation.getType();
  }

  public String getValue() {
    return annotation.getValue();
  }

  public Set<URI> getAnnotations() {
    LOG.debug("Converting {} annotations: [{}]", annotation.getAnnotations().size(), annotation.getAnnotations());
    // TODO: When Jackson can handle Streams, maybe return Stream<AnnotationView>.
    final Set<URI> uris = Sets.newHashSet(annotation.streamAnnotations().map(a -> annotationURI(a)).iterator());
    LOG.debug("uris: {}", uris);
    return uris;
  }

  private URI annotationURI(AlexandriaAnnotation a) {
    LOG.debug("annotationURI for: [{}], id=[{}]", a, a.getId());
    final String annotationId = a.getId().toString();
    return UriBuilder.fromUri(config.getBaseURI()).path(ANNOTATIONS_PATH).path(annotationId).build();
  }

  public String getCreatedOn() {
    return annotation.getCreatedOn().toString(); // ISO-8601 representation
  }

}