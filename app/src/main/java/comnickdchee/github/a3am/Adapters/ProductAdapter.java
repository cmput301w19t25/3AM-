package comnickdchee.github.a3am.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import comnickdchee.github.a3am.Company;
import comnickdchee.github.a3am.Product;
import comnickdchee.github.a3am.R;
import comnickdchee.github.a3am.ViewHolders.CompanyViewHolder;
import comnickdchee.github.a3am.ViewHolders.ProductViewHolder;

/*
 * Copyright (C) 2018 Levi Rizki Saputra (levirs565@gmail.com).
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
 *
 * Created by LEVI on 22/09/2018.
 */
public class ProductAdapter extends ExpandableRecyclerViewAdapter<CompanyViewHolder, ProductViewHolder> {
    public ProductAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CompanyViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Product product = (Product) group.getItems().get(childIndex);
        holder.bind(product);
    }

    @Override
    public void onBindGroupViewHolder(CompanyViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Company company = (Company) group;
        holder.bind(company);
    }
}

