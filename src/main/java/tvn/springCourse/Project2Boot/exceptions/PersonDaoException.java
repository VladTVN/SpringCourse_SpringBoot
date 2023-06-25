package tvn.springCourse.Project2Boot.exceptions;


import tvn.springCourse.Project2Boot.enums.DaoErrorCode;

public class PersonDaoException extends Exception{
    private DaoErrorCode errorCode = DaoErrorCode.OK;

    public PersonDaoException() {
    }

    public PersonDaoException(DaoErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DaoErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(DaoErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String errorMessage(){
        if (errorCode.equals(DaoErrorCode.OK)){
            return "No errors were found";
        }
        if (errorCode.equals(DaoErrorCode.ENTITY_NOT_FOUND)){
            return "The person was not found in the database";
        }
        return "";
    }

}
