package com.tdd.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.tdd.user.gateways.httpclient.SerasaIntegrationStatus;

public class SerasaIntegrationStatusFixtures implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(SerasaIntegrationStatus.class).addTemplate("pending debit", new Rule() {{
            add("codigoDeStatus", "1");
        }});

        Fixture.of(SerasaIntegrationStatus.class).addTemplate("no debit", new Rule() {{
            add("codigoDeStatus", "2");
        }});
    }
}
