/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dlp;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
// CHECKSTYLE OFF: AbbreviationAsWordInName
public class TemplatesIT {
    // CHECKSTYLE ON: AbbreviationAsWordInName

  private ByteArrayOutputStream bout;
  private PrintStream out;

  @Before
  public void setUp() {
    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);
    assertNotNull(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
  }

  @After
  public void tearDown() {
    System.setOut(null);
    bout.reset();
  }

  @Test
  public void testCreateInspectTemplate() throws Exception {
    Templates.main(new String[] {
        "-c",
        "-displayName", String.format("test-name-%s", new Date()),
        "-templateId", String.format("template%s", System.currentTimeMillis()),
        "-description", String.format("description-%s", new Date())
    });
    String output = bout.toString();
    assertThat(output, containsString("Template created: "));
  }

  @Test
  public void testListInspectemplate() throws Exception {
    Templates.main(new String[] {
        "-l"
    });
    String output = bout.toString();
    assertThat(output, containsString("Template name:"));
  }

  @Test
  public void testDeleteInspectTemplate() throws Exception {
    // Extract a Template ID
    Templates.main(new String[] { "-l" });
    String output = bout.toString();
    Matcher templateIds = Pattern.compile("template[0-9]+").matcher(output);
    assertTrue(templateIds.find());
    String templateId = templateIds.group(0);
    bout.reset();
    Templates.main(new String[] {
        "-d",
        "-templateId", templateId
    });
    output = bout.toString();
    assertThat(output, containsString("Deleted template:"));
  }

}
