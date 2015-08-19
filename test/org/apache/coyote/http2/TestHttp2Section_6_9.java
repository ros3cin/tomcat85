/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.coyote.http2;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for Section 6.9 of
 * <a href="https://tools.ietf.org/html/rfc7540">RFC 7540</a>.
 * <br>
 * The order of tests in this class is aligned with the order of the
 * requirements in the RFC.
 */
public class TestHttp2Section_6_9 extends Http2TestBase {

    @Test
    public void testZeroWindowUpdateConnection() throws Exception {
        http2Connect();

        sendWindowUpdate(0, 0);

        parser.readFrame(true);

        Assert.assertTrue(output.getTrace(), output.getTrace().startsWith(
                "0-Goaway-[1]-[" + Http2Error.PROTOCOL_ERROR.getCode() + "]-["));
    }


    @Test
    public void testZeroWindowUpdateStream() throws Exception {
        http2Connect();

        sendPriority(3,  0,  15);

        sendWindowUpdate(3, 0);

        parser.readFrame(true);

        Assert.assertEquals("3-RST-[" + Http2Error.PROTOCOL_ERROR.getCode() + "]",
                output.getTrace());
    }


    @Test
    public void testWindowUpdateOnClosedStream() throws Exception {
        http2Connect();

        // Should not be an error so should be nothing to read
        sendWindowUpdate(1, 200);

        // So the next request should process normally
        sendSimpleGetRequest(3);
        readSimpleGetResponse();
        Assert.assertEquals(getSimpleResponseTrace(3), output.getTrace());
    }


    // TODO: Test always accounting for changes in flow control windows even if
    //       the frame is in error.


    @Test
    public void testWindowUpdateWrongLength() throws Exception {
        http2Connect();

        byte[] zeroLengthWindowFrame = new byte[9];
        // Length zero
        ByteUtil.setOneBytes(zeroLengthWindowFrame, 3, FrameType.WINDOW_UPDATE.getIdByte());
        // No flags
        // Stream 1
        ByteUtil.set31Bits(zeroLengthWindowFrame, 5, 1);

        os.write(zeroLengthWindowFrame);
        os.flush();

        parser.readFrame(true);

        Assert.assertTrue(output.getTrace(), output.getTrace().startsWith(
                "0-Goaway-[1]-[" + Http2Error.FRAME_SIZE_ERROR.getCode() + "]-["));
    }

    // TODO: Remaining 6.9 tests
}