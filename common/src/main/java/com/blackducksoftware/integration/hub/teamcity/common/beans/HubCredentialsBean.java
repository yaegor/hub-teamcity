package com.blackducksoftware.integration.hub.teamcity.common.beans;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.suite.sdk.util.PasswordDecrypter;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("globalHubCredentials")
public class HubCredentialsBean implements Serializable {

    private String hubUser;

    private String hubPass;

    private Integer actualPasswordLength;

    public HubCredentialsBean(String hubUser) {
        this.hubUser = hubUser;
    }

    public HubCredentialsBean(String hubUser, String password) {
        this.hubUser = hubUser;
        hubPass = password;
    }

    public HubCredentialsBean(HubCredentialsBean credentials) {
        if ((credentials != null) && !credentials.isEmpty()) {
            hubUser = credentials.getHubUser();

            hubPass = credentials.getEncryptedPassword();
        }
    }

    public String getHubUser() {
        return hubUser;
    }

    public void setHubUser(String hubUser) {
        this.hubUser = hubUser;
    }

    public String getEncryptedPassword() {
        return hubPass;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        hubPass = encryptedPassword;
    }

    public Integer getActualPasswordLength() {
        return actualPasswordLength;
    }

    public void setActualPasswordLength(Integer actualPasswordLength) {
        this.actualPasswordLength = actualPasswordLength;
    }

    public String getMaskedPassword() throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (StringUtils.isBlank(hubPass)) {
            return null;
        }

        if (actualPasswordLength == null) {
            String password = getDecryptedPassword();
            if (password != null) {
                actualPasswordLength = password.length();
            }
        }
        if (actualPasswordLength != null) {
            char[] array = new char[actualPasswordLength];
            Arrays.fill(array, '*');
            return new String(array);
        } else {
            return "";
        }
    }

    public String getDecryptedPassword() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String encryptedPassword = hubPass;

        ClassLoader originalClassLoader = Thread.currentThread()
                .getContextClassLoader();
        boolean changed = false;
        try {

            if (PasswordDecrypter.class.getClassLoader() != originalClassLoader) {
                changed = true;
                Thread.currentThread().setContextClassLoader(PasswordDecrypter.class.getClassLoader());
            }

            Method method = PasswordDecrypter.class.getDeclaredMethod("decrypt", String.class);
            method.setAccessible(true);
            Object result = method.invoke(null, encryptedPassword);
            if (result != null) {
                return String.valueOf(result);
            } else {
                return null;
            }
        } finally {
            if (changed) {
                Thread.currentThread().setContextClassLoader(
                        originalClassLoader);
            }
        }
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(hubUser) && StringUtils.isBlank(hubPass);
    }

    @Override
    public String toString() {
        return "CCCredentialsBean [hubUser=" + hubUser + ", hubPass=" + hubPass + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hubPass == null) ? 0 : hubPass.hashCode());
        result = prime * result + ((hubUser == null) ? 0 : hubUser.hashCode());
        result = prime * result + ((actualPasswordLength == null) ? 0 : actualPasswordLength);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HubCredentialsBean other = (HubCredentialsBean) obj;
        if (hubPass == null) {
            if (other.hubPass != null) {
                return false;
            }
        } else if (!hubPass.equals(other.hubPass)) {
            return false;
        }
        if (hubUser == null) {
            if (other.hubUser != null) {
                return false;
            }
        } else if (!hubUser.equals(other.hubUser)) {
            return false;
        }
        if (actualPasswordLength == null) {
            if (other.actualPasswordLength != null) {
                return false;
            }
        } else if (actualPasswordLength != other.actualPasswordLength) {
            return false;
        }
        return true;
    }

}
