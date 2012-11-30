package org.phiresearchlab.commsphere.shared;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 17, 2011
 *
 */
public enum TaskStatus {
    NotStarted { public String toString() { return "Not Started"; } },
    Complete { public String toString() { return "Complete"; } }, 
    InDevelopment { public String toString() { return "In Development"; } }, 
    InProgress { public String toString() { return "In Progress"; } } 
}
