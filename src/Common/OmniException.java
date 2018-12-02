package Common;

/** General exception class. One of these being thrown indicates something
  * failed somewhere -- usually a file access -- and the program needs to
  * gracefully recover or abort.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class OmniException extends Exception{



// REASON CODES ================================================================
/** Reason the OmniException was thrown. */
public enum ReasonCode{
  UNSPECIFIED      ,
  DEBUG            ,
  FILE_UNSPECIFIED ,
  FILE_ACCESS      ,
  FILE_READ_ERROR  ,
  FILE_WRITE_ERROR ,
  OTHER
};
/** Reason code -- no reason given. */
public static final ReasonCode UNSPECIFIED      = ReasonCode.UNSPECIFIED;
/** Reason code -- debugging exception. */
public static final ReasonCode DEBUG            = ReasonCode.DEBUG;
/** Reason code -- unspecified file issue. */
public static final ReasonCode FILE_UNSPECIFIED = ReasonCode.FILE_UNSPECIFIED;
/** Reason code -- file not found / cannot be opened. */
public static final ReasonCode FILE_ACCESS      = ReasonCode.FILE_ACCESS;
/** Reason code -- file read error / malformed / unexpected end-of-file. */
public static final ReasonCode FILE_READ_ERROR  = ReasonCode.FILE_READ_ERROR;
/** Reason code -- file write error. */
public static final ReasonCode FILE_WRITE_ERROR = ReasonCode.FILE_WRITE_ERROR;
/** Reason code -- other category. */
public static final ReasonCode OTHER            = ReasonCode.OTHER;

private ReasonCode reason = ReasonCode.UNSPECIFIED;
// REASON CODES ================================================================


// OmniException() =============================================================
/** Creates a new OmniException for unspecified reason and no message.        */
public OmniException(){super();}


/** Creates a new OmniException for unspecified reason but with a message.
  * @param message Human-readable message explaining exception.               */
public OmniException(String message){super(message);}


/** Creates a new OmniException for a specified reason but no message.
 * @param reasonV Reason code for the exception.                              */
public OmniException(ReasonCode reasonV){
  super();
  reason = reasonV;
}

/** Creates a new OmniException for a specified reason and a message.
  * @param reasonV Reason code for the exception.
  * @param message Human-readable message explaining exception.               */
public OmniException(ReasonCode reasonV,String message){
  super(message);
  reason = reasonV;
}
// OmniException() =============================================================


// getReason() =================================================================
/** Get the reason code for the exception.
  * @return Reason code for the exception.                                    */
public ReasonCode getReason(){return reason;}
// getReason() =================================================================



} // class OmniException
