package org.swas.web.view;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class TilesUrlBasedViewResolver extends UrlBasedViewResolver {

    private String tilesDefinitionName = null;
    private String tilesBodyAttributeName = null;
    private String tilesDefinitionDelimiter = null;

    public void setTilesDefinitionName(String tilesDefinitionName) {
        this.tilesDefinitionName = tilesDefinitionName;
    }

    public void setTilesBodyAttributeName(String tilesBodyAttributeName) {
        this.tilesBodyAttributeName = tilesBodyAttributeName;
    }

    public void setTilesDefinitionDelimiter(String tilesDefinitionDelimiter) {
        this.tilesDefinitionDelimiter = tilesDefinitionDelimiter;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = super.buildView(viewName);
        if (view instanceof DynamicTilesView) {
            DynamicTilesView dtv = (DynamicTilesView) view;
            if (StringUtils.hasLength(tilesDefinitionName)) {
                dtv.setTilesDefinitionName(tilesDefinitionName);
            }
            if (StringUtils.hasLength(tilesBodyAttributeName)) {
                dtv.setTilesBodyAttributeName(tilesBodyAttributeName);
            }
            if (tilesDefinitionDelimiter != null) {
                dtv.setTilesDefinitionDelimiter(tilesDefinitionDelimiter);
            }
        }
        return view;

    }


}
