package br.com.actia.event;

import service.FileToCopy;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class CopyFileEvent extends AbstractEvent<FileToCopy> {
    public CopyFileEvent(FileToCopy target) {
        super(target);
    }
}
