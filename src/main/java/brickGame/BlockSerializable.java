package brickGame; // Package declaration

import java.io.Serializable; // Importing necessary Java classes for serialization

public class BlockSerializable implements Serializable { // Class for serializing Block objects
    public final int row; // Member variable declaration for row position
    public final int j; // Member variable declaration, possibly for column position
    public final int type; // Member variable declaration for type of the block

    public BlockSerializable(int row, int j, int type) { // Constructor for BlockSerializable class
        this.row = row; // Assigning the row position
        this.j = j; // Assigning the column position
        this.type = type; // Assigning the type of the block
    }
}
