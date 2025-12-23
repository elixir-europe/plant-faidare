import { expect, test } from '@playwright/test';

test.describe('Trial', () => {
  test('should display the trial page and export', async ({ page, context }) => {
    test.setTimeout(45_000);
    await page.goto('/faidare-dev/trials/dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=');

    await expect(page.getByRole('heading', { level: 1 })).toHaveText('Trial Network: Drops Phenotyping Network');

    await page.getByRole('button', { name: 'Export observations' }).click();
    await page.getByRole('button', { name: 'VIRTUAL_TRIAL' }).click();

    await expect(page.getByRole('heading', { level: 1, name: 'Export observations for trial Drops Phenotyping Network' })).toBeVisible();

    // check all years
    await page.locator('#season-names').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('2011')).toBeChecked();
    await expect(page.getByLabel('2012')).toBeChecked();
    await expect(page.getByLabel('2013')).toBeChecked();

    // uncheck all years
    await page.locator('#season-names').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('2011')).not.toBeChecked();
    await expect(page.getByLabel('2012')).not.toBeChecked();
    await expect(page.getByLabel('2013')).not.toBeChecked();

    // check all study locations
    await page.locator('#study-locations').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('Bologna')).toBeChecked();
    await expect(page.getByLabel('Campagnola')).toBeChecked();

    // uncheck all study locations
    await page.locator('#study-locations').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('Bologna')).not.toBeChecked();
    await expect(page.getByLabel('Campagnola')).not.toBeChecked();

    // check all observation variable names
    await page.locator('#observation-variable-names').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('L10')).toBeChecked();
    await expect(page.getByLabel('L11')).toBeChecked();

    // uncheck all observation variable names
    await page.locator('#observation-variable-names').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('L10')).not.toBeChecked();
    await expect(page.getByLabel('L11')).not.toBeChecked();

    await expect(page.getByLabel('Excel')).toBeChecked();

    // fill the form
    await page.getByLabel('2013').check();
    await page.getByLabel('Gaillac').check();
    await page.getByLabel('Psi_Fill').check();
    await page.getByLabel('CSV').check();

    const exportPagePromise = context.waitForEvent('page');

    await page.getByRole('button', { name: 'Export', exact: true }).click();
    await expect(page.getByRole('heading', { level: 1, name: 'Export observations for trial Drops Phenotyping Network' })).not.toBeVisible();

    const exportPage = await exportPagePromise;

    await expect(exportPage.getByRole('heading', { level: 1 })).toHaveText('Export for trial Network: Drops Phenotyping Network');
    await expect(exportPage.getByRole('alert')).toContainText('The export is in progress.');

    await expect(exportPage.getByRole('alert')).toContainText('The export succeeded.', { timeout: 30_000 });

    const downloadPromise = exportPage.waitForEvent('download');
    await exportPage.getByRole('button', { name: 'Download' }).click();

    const download = await downloadPromise;
    expect(download.suggestedFilename().endsWith('.csv')).toBe(true);
  });
});
