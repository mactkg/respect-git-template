package net.makerbox.rgt;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.Refreshable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UseGitTemplateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String basePath = e.getProject().getBasePath();
        TemplateFinder finder = new TemplateFinder();
        if(basePath != null) {
            finder.setBasePath(basePath);
        }
        String template = finder.getTemplate();
        final CommitMessageI commitPanel = getCommitPanel(e);
        if (commitPanel == null) {
            return;
        }

        commitPanel.setCommitMessage(template);
    }

    @Nullable
    private static CommitMessageI getCommitPanel(@Nullable AnActionEvent e) {
        if (e == null) {
            return null;
        }
        Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (data instanceof CommitMessageI) {
            return (CommitMessageI) data;
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
    }
}
