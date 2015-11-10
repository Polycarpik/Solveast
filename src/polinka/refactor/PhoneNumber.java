package polinka.refactor;

public class PhoneNumber {
    private Number number;

    public PhoneNumber(String number) {
        this.number = new Number(number);
    }

    public String getNumber() {
        return number.getNumber();
    }

    public void setNumber(String number) {
        this.number = new Number(number);
    }
}

