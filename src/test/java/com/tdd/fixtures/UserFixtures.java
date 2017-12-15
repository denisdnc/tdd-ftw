package com.tdd.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.tdd.user.domains.User;

public class UserFixtures implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(User.class).addTemplate("valid", new Rule() {{
            add("name", "Jack");
            add("document", "123456789");
        }});
    }
}
