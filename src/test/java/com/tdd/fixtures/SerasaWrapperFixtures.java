package com.tdd.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.tdd.user.domains.SerasaWrapper;
import com.tdd.user.domains.User;

public class SerasaWrapperFixtures implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(SerasaWrapper.class).addTemplate("pendente", new Rule() {{
            add("status", "PENDING");
        }});

    }
}
