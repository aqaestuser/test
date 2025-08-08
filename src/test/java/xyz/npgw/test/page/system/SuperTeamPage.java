package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.common.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.common.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;

@Log4j2
public class SuperTeamPage extends BaseTeamPage<SuperTeamPage>
        implements SuperHeaderMenuTrait<SuperTeamPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<SuperTeamPage> {

    public SuperTeamPage(Page page) {
        super(page);
    }
}
