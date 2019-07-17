package handson.impl;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductProjectionType;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.queries.ProductQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryPredicate;

/**
 * This class provides query operations for {@link ProductProjection}s.
 */
public class ProductQueryService extends AbstractService {

	public ProductQueryService(SphereClient client) {
		super(client);
	}

	/**
	 * @param locale
	 * @param name
	 * @return
	 */
	private CompletionStage<PagedQueryResult<Category>> findCategory(final Locale locale, final String name) {
		// TODO 4.1 Find a category

		CompletionStage<PagedQueryResult<Category>> category = client.execute(CategoryQuery.of().byName(locale, name));
		return category;
	}

	/**
	 * Queries product projections that belong to given category
	 * 
	 * @param category
	 * @return Paged result of Product projections
	 */
	private CompletionStage<PagedQueryResult<ProductProjection>> withCategory(final Category category) {

		final ProductProjectionQuery exists = ProductProjectionQuery.ofStaged()
				.withPredicates(m -> m.categories().isIn(Arrays.asList(category)));

		CompletionStage<PagedQueryResult<ProductProjection>> productsWithCategory = client.execute(exists);

		return productsWithCategory;
		// TODO 4.2 Query a category

	}

	/**
	 * Finds products with categories that have the given localized name.
	 *
	 * @param locale
	 *            the locale
	 * @param name
	 *            the localized name
	 * @return the product query completion stage
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public CompletionStage<PagedQueryResult<ProductProjection>> findProductsWithCategory(final Locale locale,
			final String name) throws InterruptedException, ExecutionException {

		CompletionStage<PagedQueryResult<Category>> categoryCompletionStage = findCategory(locale, name);
		Category category = null;
		try {
			category = categoryCompletionStage.toCompletableFuture().get().getResults().get(0);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		CompletionStage<PagedQueryResult<ProductProjection>> productsWithCategory = withCategory(category);
		
		// Issue over here... how to filter with Locale ....
		
		//PagedQueryResult<ProductProjection> pagedQueryResult  = productsWithCategory.toCompletableFuture().get();
		

		return productsWithCategory;
		// TODO 4.3 Find a product with category

	}

}
