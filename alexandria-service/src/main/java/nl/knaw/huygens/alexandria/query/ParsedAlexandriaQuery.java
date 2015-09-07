package nl.knaw.huygens.alexandria.query;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import nl.knaw.huygens.alexandria.storage.Storage;
import nl.knaw.huygens.alexandria.storage.frames.AlexandriaVF;
import nl.knaw.huygens.alexandria.storage.frames.AnnotationVF;

public class ParsedAlexandriaQuery {
  // this is just a container class for the results of processing the AlexandriaQuery parameters
  private static final Function<Storage, Stream<AnnotationVF>> DEFAULT_ANNOTATIONVF_FINDER = storage -> {
    Iterable<AnnotationVF> iterable = () -> storage.find(AnnotationVF.class);
    Stream<AnnotationVF> stream = StreamSupport.stream(iterable.spliterator(), false);
    return stream;
  };

  private Class<? extends AlexandriaVF> vfClazz;
  private List<String> returnFields;
  private Predicate<AnnotationVF> predicate;
  private Comparator<AnnotationVF> comparator;
  private Function<AnnotationVF, Map<String, Object>> mapper;
  private Function<Storage, Stream<AnnotationVF>> annotationVFFinder = DEFAULT_ANNOTATIONVF_FINDER;

  public ParsedAlexandriaQuery setVFClass(Class<? extends AlexandriaVF> vfClass) {
    this.vfClazz = vfClass;
    return this;
  }

  public Class<? extends AlexandriaVF> getVFClass() {
    return this.vfClazz;
  }

  public void setReturnFields(List<String> returnFields) {
    this.returnFields = returnFields;
  }

  public List<String> getReturnFields() {
    return returnFields;
  }

  public void setResultMapper(Function<AnnotationVF, Map<String, Object>> mapper) {
    this.mapper = mapper;
  }

  public Function<AnnotationVF, Map<String, Object>> getResultMapper() {
    return mapper;
  }

  public ParsedAlexandriaQuery setPredicate(Predicate<AnnotationVF> predicate) {
    this.predicate = predicate;
    return this;
  }

  public Predicate<AnnotationVF> getPredicate() {
    return predicate;
  }

  public ParsedAlexandriaQuery setResultComparator(Comparator<AnnotationVF> comparator) {
    this.comparator = comparator;
    return this;
  }

  public Comparator<AnnotationVF> getResultComparator() {
    return comparator;
  }

  public void setAnnotationVFFinder(Function<Storage, Stream<AnnotationVF>> annotationVFFinder) {
    this.annotationVFFinder = annotationVFFinder;
  }

  public Function<Storage, Stream<AnnotationVF>> getAnnotationVFFinder() {
    return annotationVFFinder;
  }

}