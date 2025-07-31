/*
 * MIT License
 *
 * Copyright (c) 2025 Interguess.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.interguess.autoimpl.annotationprocessor.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceFileLoaderUtilTest {

    @Test
    public void testLoadResourceFile() {
        Assertions.assertEquals(
                """
                        FZ wv3&AlFx1PR|im 5m7bNL
                        Ff1UIdPOe|NR/ gw TpAAvfh
                        KE7U nU| L$yMeykZxGogsl9
                        lNDiFcnWgE5O e4|d GOjjG
                        oJF Q oDb7xqf|/we5Bso1bM
                        GgX5cp4qb|§c AL i0x1HN3r
                        ZzvoRGTZufTfke!  uX|KQKN
                        noTFnreOI 7ga 74Lkr|J^WL
                        4mpBA I4G 7p|cMAH9_4cQ3Z
                        piWZAUSZ:Jtt6a8qF N |Bfy""",
                ResourceFileLoaderUtil.load("/test-1.txt")
        );

        Assertions.assertEquals("sLyurr0tIQrIAjx6ClS7", ResourceFileLoaderUtil.load("/test-2.txt"));

        Assertions.assertThrows(RuntimeException.class, () -> ResourceFileLoaderUtil.load("test1.txt"));
        Assertions.assertThrows(RuntimeException.class, () -> ResourceFileLoaderUtil.load("test-2.txt"));
        Assertions.assertThrows(RuntimeException.class, () -> ResourceFileLoaderUtil.load("tes/t"));
    }
}
