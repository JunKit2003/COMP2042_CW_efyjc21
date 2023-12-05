package brickGame; // Package declaration

import java.io.Serializable; // Importing necessary Java classes for serialization


/**
 * Represents a serializable form of a block in the brick game.
 * This class is designed to serialize the state of a block for purposes such as saving game state or network transmission.
 */
public class BlockSerializable implements Serializable { // Class for serializing Block objects
    /** The row position of the block in the game grid. */
    public final int row; // Member variable declaration for row position

    /** The column position or another significant integer value related to the block. The exact meaning of 'j' should be clarified based on game logic. */
    public final int j; // Member variable declaration, possibly for column position

    /** The type of the block, indicating its behavior or characteristics in the game. */

    public final int type; // Member variable declaration for type of the block



    /**
     * Constructs a new instance of BlockSerializable with specified properties.
     *
     * @param row The row position of the block in the game grid.
     * @param j The column position (or another significant integer value related to the block; this should be clarified based on the actual use of 'j' in the game logic).
     * @param type The type of the block, which may affect its behavior or interaction in the game.
     */
    public BlockSerializable(int row, int j, int type) { // Constructor for BlockSerializable class
        this.row = row; // Assigning the row position
        this.j = j; // Assigning the column position
        this.type = type; // Assigning the type of the block
    }
}
