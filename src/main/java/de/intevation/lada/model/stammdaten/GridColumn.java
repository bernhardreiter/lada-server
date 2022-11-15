/* Copyright (C) 2018 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.model.stammdaten;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grid_col_mp", schema = SchemaName.NAME)
public class GridColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(name = "data_index")
    private String dataIndex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_type")
    private ResultType dataType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filter")
    private Filter filter;

    private String gridCol;

    private Integer position;

    @Column(name = "base_query")
    private Integer baseQuery;

    public GridColumn() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataIndex() {
        return this.dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public ResultType getDataType() {
        return this.dataType;
    }

    public void setDataType(ResultType dataType) {
        this.dataType = dataType;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getGridCol() {
        return this.gridCol;
    }

    public void setGridCol(String gridCol) {
        this.gridCol = gridCol;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getBaseQuery() {
        return this.baseQuery;
    }

    public void setBaseQuery(Integer query) {
        this.baseQuery = query;
    }
}
