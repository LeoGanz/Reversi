package de.lmu.ifi.sosylab.fddlj.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Message}.
 *
 * @author Florian Theimer
 */
public class MessageTest {

  @Test
  public void testGetType() {
    String type = "my-type";
    Message message = new Message<>(type, new Object());

    Assertions.assertEquals(
            type,
            message.getType(),
            "getType() returned " + message.getType() + " instead of expected " + type);
  }

  @Test
  public void testGetData() {
    Object object = new Object();
    Message message = new Message<>("my-type", object);

    Assertions.assertEquals(
            object,
            message.getData(),
            "getData() returned " + message.getData() + " instead of expected " + object);
  }

}
