package nl.knaw.huygens.alexandria.storage;

import org.junit.BeforeClass;

import com.google.inject.Module;

import nl.knaw.huygens.alexandria.EndpointTest;
import nl.knaw.huygens.alexandria.service.AlexandriaService;
import nl.knaw.huygens.alexandria.service.TinkerpopAlexandriaService;

public class TinkergraphServiceEndpointTest extends EndpointTest {
  private static Storage storage = new TinkerGraphStorage();
  protected static final AlexandriaService tinkerGraphService = new TinkerpopAlexandriaService(storage);

  @BeforeClass
  public static void setup() {
    Module baseModule = new TestModule(tinkerGraphService);
    setupWithModule(baseModule);
  }

}