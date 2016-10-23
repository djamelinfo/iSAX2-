package timeseries.isax2;

import java.io.Serializable;



/**
 * Defines node types.
 *
 * @author DJAMEL
 *
 */
public enum NodeType implements Serializable{
	ROOT,
  /** The inner node type. */
  INTERNAL,
  /** The leaf node type. */
  TERMINAL;
}
