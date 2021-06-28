export class Constants {
    public static readonly PASSWORD_REGEX: string = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!#%*^()?&])[A-Za-z\\d@$#!%()^*?&]{8,32}$";
    public static readonly USERNAME_REGEX: string = "^[a-zA-Z0-9]{4,40}$";
    public static readonly EMAIL_REGEX: string = "^(.+)@(.+)\.(.+){1,40}$";
}