/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.diagnosis;

import hudson.model.AdministrativeMonitor;
import jenkins.model.Jenkins;
import hudson.Extension;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;

/**
 * If Hudson is run with a lot of jobs but no views, suggest the user that they can create views.
 *
 * <p>
 * I noticed at an user visit that some users didn't notice the '+' icon in the tab bar. 
 *
 * @author Kohsuke Kawaguchi
 */
@Extension @Symbol("tooManyJobsNoView")
public class TooManyJobsButNoView extends AdministrativeMonitor {
    public boolean isActivated() {
        Jenkins h = Jenkins.getInstance();
        return h.getViews().size()==1 && h.getItemMap().size()> THRESHOLD;
    }

    /**
     * Depending on whether the user said "yes" or "no", send him to the right place.
     */
    public void doAct(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(req.hasParameter("no")) {
            disable(true);
            rsp.sendRedirect(req.getContextPath()+"/manage");
        } else {
            rsp.sendRedirect(req.getContextPath()+"/newView");
        }
    }

    public static final int THRESHOLD = 16;
}
