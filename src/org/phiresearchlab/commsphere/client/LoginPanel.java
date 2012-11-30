package org.phiresearchlab.commsphere.client;

import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.PersonServiceAsync;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.exception.InvalidCredentialsException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Oct 20, 2011
 *
 */
public class LoginPanel extends PopupPanel
{
    private PersonServiceAsync personService = GWT.create(PersonService.class);
    
    private CommSphere controller;
    private TextBox usernameText;
    private PasswordTextBox passwordText;
    
    public LoginPanel(CommSphere parent) {
        this.controller = parent;
        this.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        
        Image background = new Image("images/login/login_panel.png");
        
        Panel usernamePanel = createUsernamePanel();
        usernamePanel.setStyleName("usernamePanel");
        
        Panel passwordPanel = createPasswordPanel();
        passwordPanel.setStyleName("passwordPanel");
        
        ImageButton loginButton = createLoginButton();
        loginButton.setStyleName("loginButton");
        
        AbsolutePanel mainPanel = new AbsolutePanel();
        mainPanel.add(background);
        mainPanel.add(usernamePanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(loginButton);
        
        setWidget(mainPanel);
        this.addStyleName("loginPanel");
        
        usernameText.setFocus(true);
        usernameText.selectAll();
        
        // This is temporary while developing
        usernameText.setText("tsavel");
        passwordText.setText("S3cr3t");
        attemptLogin();
    }
    
    @Override
    public void show() {
        usernameText.setText("");
        passwordText.setText("");
        usernameText.setFocus(true);
        super.show();
    }
    
    private void attemptLogin() {
        String username = usernameText.getText();
        String password = passwordText.getText();
        
        personService.login(username, password, new AsyncCallback<SessionID>() {
            public void onFailure(Throwable caught)
            {
                if (caught instanceof InvalidCredentialsException) {
                    usernameText.setFocus(true);
                    usernameText.selectAll();
                    Window.alert("Unrecognized username or invalid password. Please try again.");
                    return;
                }
                
                Window.alert("Login failed: " + caught.getMessage());
            }

            public void onSuccess(SessionID result)
            {
                controller.setSessionId(result);
                hide();
            }                    
        });
    }
    
    private ImageButton createLoginButton() {
        ImageButton button = new ImageButton("images/login/login_button.png", 
                                             "images/login/login_button_down.png");
        button.setHoverImage("images/login/login_button_down.png");
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                attemptLogin();
            }        
        });
        
        return button;
    }
    
    private Panel createPasswordPanel() {
        Label label = new Label("Password:");
        label.setStyleName("loginLabel");
        passwordText = new PasswordTextBox();
        passwordText.setStyleName("loginText");
        passwordText.setVisibleLength(32);
        passwordText.setMaxLength(32);
        
        passwordText.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event)
            {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) 
                    attemptLogin();
            }
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(6);
        panel.add(label);
        panel.add(passwordText);
        
        return panel;
    }
    
    private Panel createUsernamePanel() {
        Label label = new Label("Username:");
        label.setStyleName("loginLabel");
        usernameText = new TextBox();
        usernameText.setStyleName("loginText");
        usernameText.setVisibleLength(32);
        usernameText.setMaxLength(32);
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(6);
        panel.add(label);
        panel.add(usernameText);
        
        return panel;
    }
    
}
