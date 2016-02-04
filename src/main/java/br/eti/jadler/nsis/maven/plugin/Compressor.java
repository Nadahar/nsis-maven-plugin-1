/*
 * Copyright 2016 Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.eti.jadler.nsis;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
public class Compression {
    
    public enum Type {
        ZLIB,
        BZIP2,
        LZMA;
    }
    
    private Type type = Type.ZLIB;
    private Integer dicSize = 8;
    private boolean isFinal = false;
    private boolean isSolid = false;

    @Override
    public String toString() {
        String compression = "";
        
        compression += isSolid ? " /SOLID" : "";
        compression += isFinal ? " /FINAL" : "";
        compression += type;
        
        return compression;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getDicSize() {
        return dicSize;
    }

    public void setDicSize(Integer dicSize) {
        this.dicSize = dicSize;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean isSolid) {
        this.isSolid = isSolid;
    }
}
