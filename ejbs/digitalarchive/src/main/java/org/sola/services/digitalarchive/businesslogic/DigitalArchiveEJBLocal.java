/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.digitalarchive.businesslogic;

import java.util.List;
import javax.ejb.Local;
import org.sola.services.common.ejbs.AbstractEJBLocal;
import org.sola.services.digitalarchive.repository.entities.Document;
import org.sola.services.digitalarchive.repository.entities.FileInfo;
import org.sola.services.digitalarchive.repository.entities.FileBinary;

/**
 * 
 * Provides methods to work with digital archive.<br />
 * Has implementations: {@see DigitalArchiveEJB}
 */
@Local
public interface DigitalArchiveEJBLocal extends AbstractEJBLocal {
    /**
     * Returns document entity from digital archive
     * @param documentId Document ID in digital archive
     * @return Document entity
     */
    public Document getDocument(String documentId);
    
    /**
     * Returns document entity from digital archive without binary content
     * @param documentId Document ID in digital archive
     * @return Document entity
     */
    public Document getDocumentInfo(String documentId);
    
    /**
     * Creates new document in digital archive
     * @param document Document entity
     * @return Saved copy of document
     */
    public Document createDocument(Document document);
    
    /**
     * Creates new document in digital archive from the file in shared scanned folder
     * @param document Document entity
     * @param fileName File name in the shared folder
     * @return Saved copy of document
     */
    public Document createDocument(Document document, String fileName);
    
    /**
     * Saves document in digital archive
     * @param document Document entity
     * @return Saved copy of document
     */
    public Document saveDocument(Document document);
    
    /**
     * Returns binary file from the shared folder
     * @param fileName File name in the shared folder
     * @return
     */
    public FileBinary getFileBinary(String fileName);
    
    /**
     * Returns thumbnail of the file from the shared folder
     * @param fileName File name in the shared folder
     * @return
     */
    public FileBinary getFileThumbnail(String fileName);
    
    /**
     * Returns the list of all files in the shared folder
     * @return
     */
    public List<FileInfo> getAllFiles();
    
    public boolean deleteFile(String fileName);

}
