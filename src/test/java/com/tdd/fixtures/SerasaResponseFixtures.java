package com.tdd.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.tdd.user.domains.SerasaResponse;
import com.tdd.user.domains.User;

public class SerasaResponseFixtures implements TemplateLoader{

    @Override
    public void load() {
        Fixture.of(SerasaResponse.class).addTemplate("NO_DEBIT", new Rule() {{
            add("status", "NO_DEBIT");
        }});
        Fixture.of(SerasaResponse.class).addTemplate("PENDING_DEBIT", new Rule() {{
            add("status", "PENDING_DEBIT");
        }});
    }
}
