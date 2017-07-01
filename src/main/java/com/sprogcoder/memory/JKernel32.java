/**
 * Copyright (c) 2012 sprogcoder <sprogcoder@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sprogcoder.memory;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

public class JKernel32 {
    public static String getLastError() {
        return Kernel32Util.formatMessageFromLastErrorCode(Native.getLastError());
    }

    public static WinNT.HANDLE openProcess(int dwProcessId) {
        return Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_VM_OPERATION | Kernel32.PROCESS_VM_WRITE | Kernel32.PROCESS_VM_READ, false,
                dwProcessId);
    }

    public static boolean closeHandle(WinNT.HANDLE hObject) {
        return Kernel32.INSTANCE.CloseHandle(hObject);
    }

    public static boolean writeProcessMemory(WinNT.HANDLE hProcess, int lpBaseAddress, int lpBuffer[]) {
        int length = lpBuffer.length;
        Memory memory = new Memory(length);
        for (int i = 0; i < length; i++) {
            memory.setByte(i, (byte) lpBuffer[i]);
        }
        IntByReference lpNumberOfBytesWritten = new IntByReference();
        return Kernel32.INSTANCE.WriteProcessMemory(hProcess, new Pointer(lpBaseAddress), memory,
                length, lpNumberOfBytesWritten);
    }

    public static byte[] readProcessMemory(WinNT.HANDLE hProcess, int lpBaseAddress, int nSize) {
        IntByReference baseAddress = new IntByReference();
        baseAddress.setValue(lpBaseAddress);
        Memory lpBuffer = new Memory(nSize);
        boolean success = Kernel32.INSTANCE.ReadProcessMemory(hProcess, new Pointer(lpBaseAddress), lpBuffer, nSize, null);
        return success ? lpBuffer.getByteArray(0, nSize) : null;
    }
}
