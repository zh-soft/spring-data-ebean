/*
 * Copyright 2008-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.ebean.repository.query;

import io.ebean.EbeanServer;
import org.springframework.data.repository.query.DefaultParameters;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

/**
 * Abstract base class to implement {@link RepositoryQuery}.
 *
 * @author Xuegui Yuan
 */
public abstract class AbstractEbeanQuery implements RepositoryQuery {

    private final EbeanQueryMethod method;
    private final EbeanServer ebeanServer;

    /**
     * Creates a new {@link AbstractEbeanQuery} from the given {@link EbeanQueryMethod}.
     *
     * @param method
     * @param ebeanServer
     */
    public AbstractEbeanQuery(EbeanQueryMethod method, EbeanServer ebeanServer) {

        Assert.notNull(method, "EbeanQueryMethod must not be null!");
        Assert.notNull(ebeanServer, "EbeanServer must not be null!");

        this.method = method;
        this.ebeanServer = ebeanServer;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.RepositoryQuery#getQueryMethod()
     */
    public EbeanQueryMethod getQueryMethod() {
        return method;
    }

    /**
     * Returns the {@link EbeanServer}.
     *
     * @return will never be {@literal null}.
     */
    protected EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.RepositoryQuery#execute(java.lang.Object[])
     */
    public Object execute(Object[] parameters) {
        return doExecute(getExecution(), parameters);
    }

    /**
     * @param execution
     * @param values
     * @return
     */
    private Object doExecute(EbeanQueryExecution execution, Object[] values) {
        Object result = execution.execute(this, values);
        return result;
    }

    protected EbeanQueryExecution getExecution() {

        if (method.isStreamQuery()) {
            return new EbeanQueryExecution.StreamExecution();
        } else if (method.isCollectionQuery()) {
            return new EbeanQueryExecution.CollectionExecution();
        } else if (method.isSliceQuery()) {
            return new EbeanQueryExecution.SlicedExecution(method.getParameters());
        } else if (method.isPageQuery()) {
            return new EbeanQueryExecution.PagedExecution(method.getParameters());
        } else if (method.isModifyingQuery()) {
            return method.getClearAutomatically() ? new EbeanQueryExecution.ModifyingExecution(method, ebeanServer) : new EbeanQueryExecution.ModifyingExecution(method, null);
        } else {
            return new EbeanQueryExecution.SingleEntityExecution();
        }
    }

    protected ParameterBinder createBinder(Object[] values) {
        return new ParameterBinder((DefaultParameters) getQueryMethod().getParameters(), values);
    }

    protected Object createQuery(Object[] values) {
        return doCreateQuery(values);
    }

    /**
     * Creates a {@link io.ebean.Query} or {@link io.ebean.SqlQuery} instance for the given values.
     *
     * @param values must not be {@literal null}.
     * @return
     */
    protected abstract Object doCreateQuery(Object[] values);
}
