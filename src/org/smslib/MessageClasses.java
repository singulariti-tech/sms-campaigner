// SMSLib for Java v3
// A Java API library for sending and receiving SMS via a GSM modem
// or other supported gateways.
// Web Site: http://www.smslib.org
//
// Copyright (C) 2002-2008, Thanasis Delenikas, Athens/GREECE.
// SMSLib is distributed under the terms of the Apache License version 2.0
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.smslib;

/**
 * Class representing various message classes.
 */
public class MessageClasses {

    private final String s;

    private MessageClasses(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
    /**
     * Already-read messages.
     */
    public static final MessageClasses READ = new MessageClasses("READ");
    /**
     * Not-read messages.
     */
    public static final MessageClasses UNREAD = new MessageClasses("UNREAD");
    /**
     * All messages, whether read or unread.
     */
    public static final MessageClasses ALL = new MessageClasses("ALL");
}
