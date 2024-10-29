package com.example.Utils;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SequentialID implements Serializable {
    private volatile long ID = 0;
    public synchronized long getId() {
        return this.ID++;
    }
}
