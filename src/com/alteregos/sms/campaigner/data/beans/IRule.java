/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.data.beans;

/**
 *
 * @author John
 */
public interface IRule {

    String getContent();

    boolean getEnabled();

    String getPrimaryKeyword();

    String getSecondaryKeyword();
}
